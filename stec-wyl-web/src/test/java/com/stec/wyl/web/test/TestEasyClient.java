package com.stec.wyl.web.test;

import com.autodesk.client.model.Job;
import com.autodesk.client.model.JobPayloadItem;
import com.autodesk.client.model.ObjectDetails;
import com.suitbim.forge.EasyClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;

public class TestEasyClient {
    private static Log log = LogFactory.getLog(TestEasyClient.class);

    @Test
    public void testEasyClientAll() throws Exception {
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";

        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        File uploadFile = new File("D:/新阳大道.nwd");
        String objectName = "haicang2cbd.nwd";
        String localFilePath = "data/download/xinyangdadao";

        log.debug("Begin file upload! ");
        ObjectDetails objectDetails = easyClient.uploadFileByChunk(uploadFile, objectName);
        log.debug("File upload completed! ");
        log.debug("objectDetails = " + objectDetails);

        Job job = easyClient.translateFile(objectDetails.getObjectId(), JobPayloadItem.TypeEnum.SVF, 1);
        String base64urn = job.getUrn();

        boolean flag = false;
        while (!flag) {
            flag = easyClient.checkJobComplete(base64urn);
            log.debug("flag = " + flag);
            Thread.sleep(5000);
        }
        log.debug("Translate Job complete! ");
        log.debug("job = " + job);
        log.debug("Begin download derivatives!");
        File directory = new File(localFilePath);
        easyClient.downloadFileDerivatives(base64urn, directory);
    }

    @Test
    public void testInit() throws Exception {
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";

        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        log.debug("easyClient.getClientId() = " + easyClient.getClientId());
        log.debug("easyClient.getClientSecret() = " + easyClient.getClientSecret());
        log.debug("easyClient.getUploadChunkSize() = " + easyClient.getUploadChunkSize());

    }

    @Test
    public void testUploadFile() throws Exception {
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        File uploadFile = new File("D:/stec_projects/data/新阳大道.nwd");
        ObjectDetails objectDetails = easyClient.uploadFileByChunk(uploadFile, "haicangcbd-rename.nwd");
        log.debug("File upload completed! ");

        /**
         * 记录下objectDetails.getObjectId的值，发起文件转换时会需要
         */
        log.debug("objectDetails = " + objectDetails);
    }

    @Test
    public void testDownloadFile() throws Exception {
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        /**
         * Note: objectName必须带有源文件后缀名，否则会转换失败
         * 目前测试下来只支持Revit 2016及更高版本rvt
         */
        String objectName = "rac_basic_sample_project-rename.rvt";
        File downloadFile = new File("data/download/TestDownloadData.rvt");

        easyClient.downloadFile(objectName, downloadFile);

    }

    /**
     * 开启转换
     *
     * @throws Exception
     */
    @Test
    public void testTranslateFile() throws Exception {
        String urn = "urn:adsk.objects:os.object:java-easy-client-oi43bt7kdf9forropsylcwgaluffxumz/haicangcbd-rename.nwd";
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        Job job = easyClient.translateFile(urn, JobPayloadItem.TypeEnum.SVF, 1);
        log.debug("job = " + job);

        /**
         * 记录下job.getUrn()的值，后续的下载结果文件会需要
         */
        log.debug("job.getUrn() = " + job.getUrn());
    }


    @Test
    public void testCheckTranslateJob() throws Exception {
        String urn = "urn:adsk.objects:os.object:java-easy-client-oi43bt7kdf9forropsylcwgaluffxumz/haicangcbd-rename.nwd";
        String base64urn = "dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6amF2YS1lYXN5LWNsaWVudC1vaTQzYnQ3a2RmOWZvcnJvcHN5bGN3Z2FsdWZmeHVtei9yYWNfYmFzaWNfc2FtcGxlX3Byb2plY3QtcmVuYW1lLnJ2dA\n";
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);
        boolean flag = false;
        while (!flag) {
            flag = easyClient.checkJobComplete(base64urn);
            log.debug("flag = " + flag);
        }
        log.debug("Translate Job complete! ");
        Job job = easyClient.translateFile(urn, JobPayloadItem.TypeEnum.SVF, 1);
        log.debug("job = " + job);
    }

    @Test
    public void testCheckJobProgress() throws Exception {
        String urn = "urn:adsk.objects:os.object:java-easy-client-oi43bt7kdf9forropsylcwgaluffxumz/rac_basic_sample_project-rename.rvt";
        String base64urn = "dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6amF2YS1lYXN5LWNsaWVudC1vaTQzYnQ3a2RmOWZvcnJvcHN5bGN3Z2FsdWZmeHVtei9yYWNfYmFzaWNfc2FtcGxlX3Byb2plY3QtcmVuYW1lLnJ2dA\n";
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);
        String progress = easyClient.checkJobProgress(base64urn);
        log.debug("progress = " + progress);
    }


    @Test
    public void testDownloadFileDerivatives() throws Exception {
        String base64urn = "dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6amF2YS1lYXN5LWNsaWVudC1vaTQzYnQ3a2RmOWZvcnJvcHN5bGN3Z2FsdWZmeHVtei9yYWNfYmFzaWNfc2FtcGxlX3Byb2plY3QtcmVuYW1lLnJ2dA\n";
        String clientId = "oI43bT7KdF9FORroPSylCwgALUFFXumz";
        String clientSecret = "qx9oF5lG8HrdXI7o";
        EasyClient easyClient = new EasyClient(clientId, clientSecret);

        String localFilePath = "data/download/rac_basic_sample_project-rename";
        File directory = new File(localFilePath);
        easyClient.downloadFileDerivatives(base64urn, directory);
    }
}
