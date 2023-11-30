package mx.gob.imss.cit.pmc.confronta.sftp;

import com.jcraft.jsch.SftpException;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpConfronta {

    private static final Logger logger = LoggerFactory.getLogger(SftpConfronta.class);

    public static void createBaseFolder(String basePath) throws SftpException {
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(ConfrontaConstants.ZERO, basePath.length() -1);
        }
        SftpClient sftpClient = SftpClient.getSftpClient();
        sftpClient.cd("/");
        String[] folders = basePath.substring(1).split("/");
        for (String folder : folders) {
            try {
                sftpClient.cd(folder);
            } catch (SftpException e) {
                sftpClient.mkdir(folder);
                sftpClient.cd(folder);
            }
        }
    }

}
