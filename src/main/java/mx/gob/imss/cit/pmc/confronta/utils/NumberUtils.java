package mx.gob.imss.cit.pmc.confronta.utils;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;

import java.math.BigDecimal;
import java.util.List;

public class NumberUtils {

    public static Integer safeValidateInteger(Integer integer) {
        return isNotNullInteger(integer) ? integer : ConfrontaConstants.ZERO;
    }

    public static Long safeValidateLong(Long num) {
        return isNotNullLong(num) ? num : ConfrontaConstants.ZERO;
    }

    public static Integer safetyParseBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal != null ? bigDecimal.intValue() : ConfrontaConstants.ZERO;
    }

    public static Boolean isNullInteger(Integer integer) { return integer == null; }

    public static Boolean isNullLong(Long num) { return num == null; }

    public static Boolean isNotNullInteger(Integer integer) { return !isNullInteger(integer); }

    public static Boolean isNotNullLong(Long num) { return !isNullLong(num); }

    public static Integer processConsequence(Integer consequence, Integer subsidizedDays) {
        if (consequence != null) {
            return consequence == 0 || consequence > 9
                    ? ConfrontaConstants.CONSEQUENCE_EQUIVALENCE.get(consequence)
                    : consequence;
        } else {
        	if (subsidizedDays != null && subsidizedDays > 0) {
        		return ConfrontaConstants.FIRST;
        	}
        	else {
        		return ConfrontaConstants.ZERO;
        	}
            
        }
    }

    public static List<Integer> getSubDelList(Long key) {
        List<Integer> delSubDel = ConfrontaConstants.DEL_SUBDEL.get(key.intValue());
        return delSubDel.subList(1, delSubDel.size());
    }

    public static Integer getDel(Long key) {
        return ConfrontaConstants.DEL_SUBDEL.get(key.intValue()).get(ConfrontaConstants.ZERO);
    }

}
