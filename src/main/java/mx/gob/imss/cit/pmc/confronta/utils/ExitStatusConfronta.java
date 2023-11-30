package mx.gob.imss.cit.pmc.confronta.utils;

import org.springframework.batch.core.ExitStatus;

public class ExitStatusConfronta extends ExitStatus {

    public ExitStatusConfronta(String exitCode) {
        super(exitCode);
    }

    public static final ExitStatusConfronta GENERATION_FILE_FAILED = new ExitStatusConfronta("generation_file_failed");

    public static final ExitStatusConfronta UPLOAD_SFTP1_FAILED = new ExitStatusConfronta("upload_sftp1_failed");

    public static final ExitStatusConfronta UPLOAD_SFTP2_FAILED = new ExitStatusConfronta("upload_sftp2_failed");

}
