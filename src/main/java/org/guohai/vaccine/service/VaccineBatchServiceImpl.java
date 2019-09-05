package org.guohai.vaccine.service;

import org.guohai.vaccine.dao.VaccineDao;
import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.beans.VaccineUrlBean;
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
            System.out.println(urlBean.getBatchName());
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
     * @param battchNo
     * @return
     */
    @Override
    public Result<List<VaccineBatchBean>> searchVaccineBatch(String battchNo) {
        //TODO: 参数检查，长度4~10位

        battchNo = "%"+battchNo+"%";
        List<VaccineBatchBean> list = vaccineDao.searchVaccine(battchNo);
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
            lastDate=vaccineDao.getLastDate();
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
     * 处理一个页面的数据并返回 一个LIST2016~2019
     * @param batchCode
     * @param batchUrl
     * @return
     */
    private List<VaccineBatchBean> vaccineBatchQuery(Integer batchCode, String batchUrl,Integer yeah) throws IOException {
        List<VaccineBatchBean> list = new ArrayList<>();
        Document doc = Jsoup.connect(batchUrl).maxBodySize(0).get();
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
