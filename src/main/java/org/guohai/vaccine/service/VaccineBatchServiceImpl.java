package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.*;
import org.guohai.vaccine.dao.VaccineDao;
import org.guohai.vaccine.utilities.VerificationUtilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VaccineBatchServiceImpl implements VaccineBatchService {

    private static final Logger LOG  = LoggerFactory.getLogger(VaccineBatchServiceImpl.class);
    @Autowired
    VaccineDao vaccineDao;

    /**
     * 最后更新时间
     */
    private static String lastDate="";
    /**
     * nifdc数据处理
     *
     * @return
     */
    @Override
    @Transactional
    public Result<String> nifdcVaccineData(String yeah,String index) {

        String catchUrl = "https://www.nifdc.org.cn/nifdc/zhtzhl/swzppqf/shwzhppqf"+yeah+"/index"+index+".html";
        Integer iYeah = Integer.valueOf(yeah);
        Result<String> result = new Result<>(false,"");
        List<VaccineUrlBean> listUrl = null;
        try {
            listUrl = vaccinePageQuery(catchUrl,iYeah);
        } catch (IOException e) {
            e.printStackTrace();
            result.setData(result.getData()+"url:[index.html]出错\n"+e.getMessage()+"\n");
        }
        if(listUrl == null || listUrl.size() ==0){
            LOG.error("列表抓取过程失败，请检查");
            return new Result<>(false,"数据抓取失败");
        }
        for(VaccineUrlBean urlBean : listUrl) {
            // 检查库里是否存在，如不存在继续，如存在跳过
            if(vaccineDao.getBatchUrlCount(urlBean.getBatchUrl())>0) {
                continue;
            }
            vaccineDao.addBatchUrl(urlBean);
            lastDate = urlBean.getBatchDate();

            LOG.info(urlBean.getBatchName());

            List<VaccineBatchBean> listBatch = null;
            try {
                listBatch = vaccineBatchQuery(urlBean.getCode(),urlBean.getBatchUrl(),iYeah);
            } catch (IOException e) {
                result.setData(result.getData()+"url:["+urlBean.getBatchUrl()+"]出错\n"+e.getMessage()+"\n");
            }
            if(null == listBatch || listBatch.size()==0) {
                LOG.error("抓取生物制品数据时出现了不楞转换的数据，需要进行回滚操作");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setStatus(false);
                result.setData("抓取生物制品数据时出现了不楞转换的数据，需要进行回滚操作");
                return result;
            }
            LOG.info(String.format("在URL[%s]抓取到数据[%d]条",urlBean.getBatchUrl(),listBatch.size()));
            for(VaccineBatchBean batchBean: listBatch) {
                vaccineDao.addVaccineBatchData(batchBean);
            }
        }
        if ("".equals(result.getData())) {
            result.setStatus(true);
        }
        return result;
    }

    /**
     * 新版本nifdc数据处理
     *
     * @return 返回处理结果
     */
    @Override
    public Result<String> newVersionNifdcVaccineData() {
        // 数据下载基础页
        String batchUrl = "https://www.nifdc.org.cn/nifdc/xxgk/ggtzh/jwgk/index.html";
        // 假设返回结果
        Result<String> result = new Result<>(false,"");
        List<VaccineUrlBean> listUrl = null;
        try {
            listUrl = vaccinePageQuery2020(batchUrl);
        } catch (IOException e) {
            e.printStackTrace();
            result.setData(result.getData()+"url:[index.html]出错\n"+e.getMessage()+"\n");
        }
        if(listUrl == null || listUrl.size() ==0){
            LOG.error("列表抓取过程失败，请检查");
            return new Result<>(false,"数据抓取失败");
        }
        for(VaccineUrlBean urlBean : listUrl) {
            if(vaccineDao.getBatchTitleCount(String.format("%%%s%%", urlBean.getBatchName()))>0){
                continue;

            }
            // 增加
            vaccineDao.addBatchUrl(urlBean);
            // 更新 最后时间
            lastDate = urlBean.getBatchDate();

            List<VaccineBatchBean> listBatch = null;
            try {
                listBatch = vaccineBatchQuery2021(urlBean.getCode(),urlBean.getBatchUrl(), urlBean.getBatchDate());
            } catch (IOException e) {
                result.setData(result.getData()+"url:["+urlBean.getBatchUrl()+"]出错\n"+e.getMessage()+"\n");
            }
            if(null == listBatch || listBatch.size()==0) {
                LOG.error("抓取生物制品数据时出现了不楞转换的数据，需要进行回滚操作");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setStatus(false);
                result.setData("抓取生物制品数据时出现了不楞转换的数据，需要进行回滚操作");
                return result;
            }
            LOG.info(String.format("在URL[%s]抓取到数据[%d]条",urlBean.getBatchUrl(),listBatch.size()));
            for(VaccineBatchBean batchBean: listBatch) {
                vaccineDao.addVaccineBatchData(batchBean);
            }
        }
        return null;
    }


    /**
     * @param battchNo
     * @param loginCode
     * @param formId
     * @param userIP
     * @param userAgent
     * @return
     */
    @Override
    public Result<List<VaccineBatchBean>> searchVaccineBatch(String battchNo, String loginCode, String formId, String userIP, String userAgent) {
        String openId ="";
        //查用户openId
        if(loginCode!=null && loginCode.length()>1){
            if(MiniProgramServiceImpl.UserMap.get(loginCode)!=null){
                openId = MiniProgramServiceImpl.UserMap.get(loginCode).getOpenId();
            }else {
                WechatUserBean wechatUserBean = vaccineDao.getUserByLoginCode(loginCode);
                if(wechatUserBean!=null){
                    openId = wechatUserBean.getOpenId();
                    MiniProgramServiceImpl.UserMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                }
            }
        }

        if(openId.equals("")){
            LOG.error(String.format("logincode:%s没有找到对应的openid",loginCode));
        }
        //TODO: 参数检查，长度4~10位

//        battchNo = "%"+battchNo+"%";
        List<VaccineBatchBean> list = vaccineDao.searchVaccine(String.format("%%%s%%",battchNo));
        // 当loginCode为空时判定认为是测试非小程序请求不记录日志
        if (loginCode != null){
            //写日志
            VaccineAccessLog accessLog = new VaccineAccessLog(0,openId,userAgent,userIP,"search",battchNo,formId,list.size(),new Date());
            vaccineDao.addAccessLog(accessLog);
        }

        return new Result<>(true,list);
    }

    /**
     * 获取最后更新时间
     *
     * @return
     */
    @Override
    public Result<String> getLastDate() {
        if("".equals(lastDate)){
            LOG.info(String.format("进去getlast方法，进行了一次数据库请求"));
            lastDate=vaccineDao.getLastDate();
        }
        else{
            LOG.info(String.format("进去getlast方法，没有进行数据库请求"));
        }
        return new Result<>(true,lastDate);
    }

    /**
     * 适合2017~2019的格式
     * @param url
     * @return
     * @throws IOException
     */
    private List<VaccineUrlBean> vaccinePageQuery(String url, Integer yeah) throws IOException {
        List<VaccineUrlBean> listBatch = new ArrayList<>();
        String newUrl = url.replaceAll("/[^/]+$","/");
        Document doc = Jsoup.connect(url).get();
            Elements elesBatch = doc.getElementsByClass("ListColumnClass5");
            for(Element ele : elesBatch) {

                String href=newUrl+ele.getElementsByTag("a").attr("href");
                href = href.replaceAll("[^/\\.]+/\\.\\./","").replaceAll("[^/\\.]+/\\.\\./","").replaceAll("[^/\\.]+/\\.\\./","");
                String title = ele.getElementsByTag("a").attr("title");
                String date = ele.getElementsByTag("span").text();
                if(VerificationUtilities.valiadteHttpProtocol(href) && VerificationUtilities.validateEngDate(date)) {
                    listBatch.add(new VaccineUrlBean(0, href, title, date));
                }else {
                    LOG.warn(String.format("抓取列表数据时出现了异常，不再继续[%s][%s]",url,href));
                    return null;
                }

            }

        return listBatch;
    }

    /**
     * 适用于2020年11月9号以后数据
     * @param url
     * @return
     */
    private  List<VaccineUrlBean> vaccinePageQuery2020(String url) throws IOException {
        List<VaccineUrlBean> listBatch = new ArrayList<>();
        String newUrl = url.replaceAll("/[^/]+$","/");
        Document doc = Jsoup.connect(url).get();
        Elements elesBatch = doc.getElementsByClass("list").select("li");
        for(Element ele: elesBatch){
            String href=newUrl+ele.getElementsByTag("a").attr("href");
            href = href.replaceAll("[^/\\.]+/\\.\\./","").replaceAll("[^/\\.]+/\\.\\./","").replaceAll("[^/\\.]+/\\.\\./","");
            String title = ele.getElementsByTag("a").attr("title");
            String date = ele.getElementsByTag("span").text().replace("(","").replace(")","");
            if(VerificationUtilities.valiadteHttpProtocol(href) && VerificationUtilities.validateEngDate(date)) {
                listBatch.add(new VaccineUrlBean(0, href, title, date));
            }else {
                LOG.warn(String.format("抓取列表数据时出现了异常，不再继续[%s][%s]",url,href));
                return null;
            }
        }
        return listBatch;
    }



    /**
     * 处理一个页面的数据并返回 一个LIST2016~2019
     * @param batchCode
     * @param batchUrl
     * @return
     */
    private List<VaccineBatchBean> vaccineBatchQuery(Integer batchCode, String batchUrl,Integer yeah) throws IOException {
        List<VaccineBatchBean> list = new ArrayList<>();
        Document doc = Jsoup.connect(batchUrl).timeout(1000*60*10).maxBodySize(0).get();
            Elements eleDataArea = doc.select("[id=printArea]");
            Elements eleTr = eleDataArea.select("tr");
            for(int i = 1;i<eleTr.size();i++) {

                Elements ele = eleTr.get(i).select("td");
                if ("".equals(ele.get(0).text()) || "序号".equals(ele.get(0).text()) || "　".equals(ele.get(0).text()) || "序 号".equals(ele.get(0).text())) {
                    continue;
                }
                String pageCode = ele.get(0).text();
                String batchNo = ele.get(3).text();
                String expDate = ele.get(5).text();
                String issueDate = ele.get(10).text();
                //检查几个关键字段是否合规
                if(VerificationUtilities.validateInteger(pageCode) && VerificationUtilities.validateBatchNo(batchNo) &&
                    VerificationUtilities.validateChineseDate(expDate) && VerificationUtilities.validateChineseDate(issueDate)) {
                    VaccineBatchBean vbb = new VaccineBatchBean(0, pageCode, ele.get(1).text(),
                            ele.get(2).text(), batchNo, ele.get(4).text(),expDate, ele.get(6).text(),
                            ele.get(7).text(), ele.get(8).text(), ele.get(9).text(), issueDate, ele.get(11).text(),
                            ele.get(12).text(), batchCode, "", "");
                    list.add(vbb);
                }else {
                    LOG.warn( String.format("出现不合规数据URL[%s],页面内编号:%s",batchUrl,ele.get(0).text()));
                    return null;
                }
            }




        return list;
    }

    /**
     * 处理一个页面的数据并返回 一个LIST2020~
     * @param batchCode
     * @param batchUrl
     * @return
     * @throws IOException
     */
    public List<VaccineBatchBean> vaccineBatchQuery2020(Integer batchCode, String batchUrl) throws IOException {
        List<VaccineBatchBean> list = new ArrayList<>();
        Document doc = Jsoup.connect(batchUrl).timeout(1000*60*10).maxBodySize(0).get();
        Elements eleTr = doc.getElementsByClass("text");
        eleTr = eleTr.select("tr");
        for(int i = 1;i<eleTr.size();i++) {
            Elements ele = eleTr.get(i).select("td");
            if ("".equals(ele.get(0).text()) || "序号".equals(ele.get(0).text()) || "　".equals(ele.get(0).text()) || "序 号".equals(ele.get(0).text())) {
                continue;
            }
            String pageCode = ele.get(0).text();
            String batchNo = ele.get(3).text();
            String expDate = ele.get(5).text();
            String issueDate = ele.get(10).text();
            //检查几个关键字段是否合规
            if(VerificationUtilities.validateInteger(pageCode) && VerificationUtilities.validateBatchNo(batchNo) &&
                    VerificationUtilities.validateChineseDate(expDate) && VerificationUtilities.validateChineseDate(issueDate)) {
                VaccineBatchBean vbb = new VaccineBatchBean(0, pageCode, ele.get(1).text(),
                        ele.get(2).text(), batchNo, ele.get(4).text(),expDate, ele.get(6).text(),
                        ele.get(7).text(), ele.get(8).text(), ele.get(9).text(), issueDate, ele.get(11).text(),
                        ele.get(12).text(), batchCode, "", "");
                list.add(vbb);
            }else {
                LOG.warn( String.format("出现不合规数据URL[%s],页面内编号:%s",batchUrl,ele.get(0).text()));
                return null;
            }
        }
        return list;
    }

    /**
     * 处理一个页面的数据并返回 一个LIST2020~
     * @param batchCode
     * @param batchUrl
     * @return
     * @throws IOException
     */
    private List<VaccineBatchBean> vaccineBatchQuery2021(Integer batchCode, String batchUrl,String issueDate) throws IOException {
        List<VaccineBatchBean> list = new ArrayList<>();
        Document doc = Jsoup.connect(batchUrl).timeout(1000*60*10).maxBodySize(0).get();
        Elements eleTr = doc.getElementsByClass("text");
        eleTr = eleTr.select("tr");
        for(int i = 1;i<eleTr.size();i++) {
            Elements ele = eleTr.get(i).select("td");
            if ("".equals(ele.get(0).text()) || "序号".equals(ele.get(0).text()) || "　".equals(ele.get(0).text()) || "序 号".equals(ele.get(0).text())) {
                continue;
            }
            String pageCode = ele.get(0).text();
            String batchNo = ele.get(2).text();
            String expDate = ele.get(3).text();
//            String issueDate = ele.get(10).text();
            //检查几个关键字段是否合规
            if(VerificationUtilities.validateInteger(pageCode) &&
//                    VerificationUtilities.validateBatchNo(batchNo) &&
                    VerificationUtilities.validateChineseDate(expDate)) {
                VaccineBatchBean vbb = new VaccineBatchBean(0, pageCode, ele.get(1).text(),
                        "", batchNo, "",expDate, ele.get(4).text(),
                        "", ele.get(5).text(), "", issueDate, ele.get(6).text(),
                        ele.get(7).text(), batchCode, "", "");
                list.add(vbb);
            }else {
                LOG.warn( String.format("出现不合规数据URL[%s],页面内编号:%s",batchUrl,ele.get(0).text()));
                return null;
            }
        }
        return list;
    }

    /**
     * 中文日期转Date
     * @param dateSource 中文日期：
     * @return
     * @throws ParseException
     */
    private Date StringDate2Date(String dateSource) throws ParseException {
        SimpleDateFormat sdf;
        if(dateSource.indexOf("日") == -1) {
            sdf = new SimpleDateFormat("yyyy年MM月");

        }else {
            sdf = new SimpleDateFormat("yyyy年MM月dd日");
        }
        return sdf.parse(dateSource);

    }

}
