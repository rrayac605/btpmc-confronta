package mx.gob.imss.cit.pmc.confronta.services;

import java.util.Set;

public interface EmailService {

    void sendEmail(String templateName, Set<Long> keyList);

    void sendEmailGetBackupInfoFailedList(String templateName, Set<Long> keyList);
    
    void sendEmailToOOADList(String templateName, Set<Long> keyList, Set<Long> keyCorrectos);
    
    void sendEmailStepOne(String templateName);
    
    void sendEmailStepTwo(String templateName);
    
    void sendEmailStepThree(String templateName);
    
    void sendEmailStepFourFive(String templateName);
    
}
