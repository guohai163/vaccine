package org.guohai.vaccine.service;

import org.guohai.vaccine.dao.VaccineDao;
import org.guohai.vaccine.org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineUrlBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VaccineBatchServiceImpl implements VaccineBatchService {

    @Autowired
    VaccineDao vaccineDao;
    /**
     * nifdc数据处理
     *
     * @return
     */
    @Override
    public Result<String> nifdcVaccineData(String yeah,String index) {

        String catchUrl = "https://www.nifdc.org.cn/nifdc/zhtzhl/swzppqf/shwzhppqf"+yeah+"/index"+index+".html";

        Result<String> result = new Result<>(false,"");
        List<VaccineUrlBean> listUrl = null;
        try {
            listUrl = vaccinePageQuery(catchUrl);
        } catch (IOException e) {
            e.printStackTrace();
            result.setData(result.getData()+"url:[index.html]出错\n"+e.getMessage()+"\n");
        }
        for(VaccineUrlBean urlBean : listUrl) {
            //TODO: 检查库里是否存在，如不存在继续，如存在跳过
            if(vaccineDao.getBatchUrlCount(urlBean.getBatchUrl())>0) {
                continue;
            }
            vaccineDao.addBatchUrl(urlBean);
            System.out.println(urlBean.getBatchName());
            List<VaccineBatchBean> listBatch = null;
            try {
                listBatch = vaccineBatchQuery(urlBean.getCode(),urlBean.getBatchUrl(),Integer.valueOf(yeah));
            } catch (IOException e) {
                result.setData(result.getData()+"url:["+urlBean.getBatchUrl()+"]出错\n"+e.getMessage()+"\n");
            }
            System.out.println(listBatch);
            for(VaccineBatchBean batchBean: listBatch) {
                vaccineDao.addVaccineBatchData(batchBean);
            }
        }
        if ("".equals(result.getData())) {
            result.setStatus(true);
        }
        return result;
    }

    private List<VaccineUrlBean> vaccinePageQuery(String url) throws IOException {
        List<VaccineUrlBean> listBatch = new ArrayList<>();
        String newUrl = url.replaceAll("/[^/]+$","/");
        Document doc = null;

            doc = Jsoup.connect(url).get();
            Elements elesBatch = doc.getElementsByClass("ListColumnClass5");
            for(Element ele : elesBatch) {

                String href=newUrl+ele.getElementsByTag("a").attr("href");
                String title = ele.getElementsByTag("a").attr("title");
                listBatch.add(new VaccineUrlBean(0,href,title));
            }

        return listBatch;
    }

    /**
     * 处理一个页面的数据并返回 一个LIST
     * @param batchCode
     * @param batchUrl
     * @return
     */
    private List<VaccineBatchBean> vaccineBatchQuery(Integer batchCode, String batchUrl,Integer yeah) throws IOException {
        List<VaccineBatchBean> list = new ArrayList<VaccineBatchBean>();
        Document doc = null;
            doc = Jsoup.connect(batchUrl).maxBodySize(0).get();
            Elements eleDataArea = doc.select("[id=printArea]");
            Elements eleTr = eleDataArea.select("tr");
            for(int i = 1;i<eleTr.size();i++) {
                Elements ele = eleTr.get(i).select("td");
                VaccineBatchBean vbb;
                if(ele.size() == 13) {
                    vbb = new VaccineBatchBean(0, ele.get(0).text(), ele.get(1).text(),
                            ele.get(2).text(), ele.get(3).text(), ele.get(4).text(), ele.get(5).text(), ele.get(6).text(),
                            ele.get(7).text(), ele.get(8).text(), ele.get(9).text(), ele.get(10).text(), ele.get(11).text(),
                            ele.get(12).text(), batchCode, "", "");
                }
                else if(ele.size() == 12) {
                    vbb = new VaccineBatchBean(0, ele.get(0).text(), ele.get(1).text(),
                            ele.get(2).text(), ele.get(3).text(), ele.get(4).text(), ele.get(5).text(), ele.get(6).text(),
                            ele.get(7).text(), ele.get(8).text(), ele.get(9).text(), ele.get(10).text(), ele.get(11).text(),
                            "", batchCode, "", "");
                }else if(ele.size() == 11){
                    vbb = new VaccineBatchBean(0, ele.get(0).text(), ele.get(1).text(),
                            ele.get(2).text(), ele.get(3).text(), ele.get(4).text(), ele.get(5).text(), ele.get(6).text(),
                            "", ele.get(7).text(), ele.get(8).text(), ele.get(9).text(), ele.get(10).text(),
                            "", batchCode, "", "");
                }
                else {
                    vbb = new VaccineBatchBean(0, ele.get(0).text(), ele.get(1).text(),
                            ele.get(2).text(), ele.get(3).text(), ele.get(4).text(), ele.get(6).text(), ele.get(7).text(),
                            "", "", "", "ele.get(9).text()", ele.get(8).text(),
                            "", batchCode, "", "");
                }
                list.add(vbb);
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
