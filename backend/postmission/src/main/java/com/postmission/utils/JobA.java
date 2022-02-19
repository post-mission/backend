package com.postmission.utils;

import com.postmission.controller.MusicalController;
import com.postmission.model.MusicalInfo;
import com.postmission.service.MusicalService;
import org.json.simple.parser.ParseException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JobA extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(JobA.class);

    @Autowired
    MusicalService musicalService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info("매 시간 실행 될 작업 작성 공간");
        MusicalController musicalController = new MusicalController();
        try {
            List<MusicalInfo> list = musicalController.checkData();
            musicalService.saveAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}