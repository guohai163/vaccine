package org.guohai.vaccine.service;

import org.guohai.vaccine.org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineBatchBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VaccineBatchServiceImpl implements VaccineBatchService {
    /**
     * nifdc数据处理
     *
     * @return
     */
    @Override
    public Result<String> nifdcVaccineData() {
        //https://www.nifdc.org.cn/nifdc/zhtzhl/swzppqf/shwzhppqf2019/12077.html
//https://www.nifdc.org.cn/nifdc/zhtzhl/swzppqf/shwzhppqf2019/index.html
        List<String> list = vaccinePageQuery("https://www.nifdc.org.cn/nifdc/zhtzhl/swzppqf/shwzhppqf2019/index.html");
        System.out.println(list);
        return null;
    }

    private List<String> vaccinePageQuery(String url) {
        List<String> listBatch = new ArrayList<>();
        String newUrl = url.replaceAll("/[^/]+$","/");
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Elements elesBatch = doc.getElementsByClass("ListColumnClass5");
            for(Element ele : elesBatch) {
                listBatch.add(newUrl+ele.getElementsByTag("a").attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listBatch;
    }

    /**
     * 处理一个页面的数据并返回 一个LIST
     * @param batchCode
     * @param batchUrl
     * @return
     */
    private List<VaccineBatchBean> vaccineBatchQuery(Integer batchCode, String batchUrl) {
        List<VaccineBatchBean> list = new ArrayList<VaccineBatchBean>();
        Document doc = null;
        try {
            doc = Jsoup.connect(batchUrl).get();
            Elements eleDataArea = doc.select("[id=printArea]");
            Elements eleTr = eleDataArea.select("tr");
            VaccineBatchBean a = new VaccineBatchBean();
            a.setExpDate(new Date());
            list.add(a);
            for(int i = 1;i<eleTr.size();i++) {
                Elements ele = eleTr.get(i).select("td");
                System.out.println(i);
                VaccineBatchBean vbb = new VaccineBatchBean(0,ele.get(0).text(),ele.get(1).text(),
                        ele.get(2).text(),ele.get(3).text(),ele.get(4).text(), StringDate2Date(ele.get(5).text()),ele.get(6).text(),
                        ele.get(7).text(),ele.get(8).text(),ele.get(9).text(),ele.get(10).text(),ele.get(11).text(),
                        ele.get(12).text(),batchCode,"","");
                list.add(vbb);
            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
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
