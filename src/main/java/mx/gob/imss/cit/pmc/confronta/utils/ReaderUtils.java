package mx.gob.imss.cit.pmc.confronta.utils;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.BackupConfrontaDTO;
import mx.gob.imss.cit.pmc.confronta.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.confronta.dto.MovementDTO;
import mx.gob.imss.cit.pmc.confronta.enums.*;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.stream.Stream;

public class ReaderUtils {

    public static String buildMovementsJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
        int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
        String currentDate = DateUtils.getCurrentMongoDateString(isReprocess);
        return "{ '$and' : [" +
                "    { 'aseguradoDTO.fecAlta': { $lte: ISODate('" + currentDate + "') } }," +
                "    { 'aseguradoDTO.fecBaja' : null}," +
                "    { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [1, 5, 4, 8]}}," +
                "    { '$or' : [{ 'isPending' : null}, { 'isPending' : false}]}," +
                "    { 'aseguradoDTO.numCicloAnual': '" + revisionYear +"' }," +
                "    { 'incapacidadDTO.cveConsecuencia' : { '$in' : [null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]}}," +
                "    { 'patronDTO.cveDelRegPatronal' : " + del + "}," +
                "    { 'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}}" +
                "]}";
    }
    
    public static String buildMovementsDescartadoJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
        int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
        return  "{"
				+ "    \"patronDTO.cveDelRegPatronal\" : " + del + " ," 
				+ "    'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + " },"
			    + "    'aseguradoDTO.numCicloAnual' : '" + revisionYear + "', "  
				+ "    '$or' : ["
				+ "        { "
				+ "            '$and' : ["
				+ "                { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [2, 6, 3, 7] } },"
				+ "                { '$or' : ["
				+ "                    { 'isPending' : true },"
				+ "                    { 'isPending' : { '$exists' : false  } }"
				+ ""
				+ "                ] }"
				+ "            ] "
				+ "        },"
				+ "        {"
				+ "            '$and' : ["
				+ "                { 'aseguradoDTO.cveEstadoRegistro' : { '$in' : [10, 11] } }"
				+ "            ]"
				+ "        }"
				+ "    ]"
				+ "}";
        
    }
    
	public static String buildMovementsDescartadosAnteriorJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
		int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
		return "{ " 
				+ "    'aseguradoDTO.numCicloAnual' : { '$lt' : '" + revisionYear + "' }, "
				+ "	   \"patronDTO.cveDelRegPatronal\" : " + del + " ,"
				+ "    'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + " },"
				+ "	    \"$or\":[ "
				+ "          { "
				+ "             \"isPending\":false "
				+ "          }, "
				+ "          { "
				+ "             \"isPending\":{ "
				+ "                \"$exists\":false "
				+ "             } "
				+ "          } "
				+ "       ]" 
				+ "}";

	}

	public static String buildMovementsDescartadosPosteriorJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
		int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
		return "{ " 
		+ "    'aseguradoDTO.numCicloAnual' : { '$gt' : '" + revisionYear + "' }, "
		+ "	   \"patronDTO.cveDelRegPatronal\" : " + del + " ,"
		+ "    'patronDTO.cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + " },"
		+ "	    \"$or\":[ "
		+ "          { "
		+ "             \"isPending\":false "
		+ "          }, "
		+ "          { "
		+ "             \"isPending\":{ "
		+ "                \"$exists\":false "
		+ "             } "
		+ "          } "
		+ "       ]" 
		+ "}";

	}


    public static String buildChangesJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
        int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
        String currentDate = DateUtils.getCurrentMongoDateString(isReprocess);
        return "{'$and' : [" +
                "    { 'fecAlta': { $lte: ISODate('" + currentDate + "') } }," +
                "    { 'cveOrigenArchivo' : 'MN'}," +
                "    { 'fecBaja' : null}," +
                "    { 'cveEstadoRegistro' : { '$in' : [1, 5, 4, 8]}}," +
                "    { '$or' : [{ 'isPending' : null}, { 'isPending' : false}]}," +
                "    { 'cveSituacionRegistro' : 1}," +
                "    { 'cveConsecuencia' : { '$in' : [null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]}}," +
                "    { 'numCicloAnual': '" + revisionYear + "' }," +
                "    { 'cveDelRegPatronal' : " + del +" }," +
                "    { 'cveSubDelRegPatronal' : { '$in' : " + subDelList.toString() + "}}" +
                "]}";
    }
    
    public static String buildChangesDescartadosJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
    	int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
        return  "{"
        		+ "    cveDelRegPatronal : " + del + " , "
        		+ "    cveSubDelRegPatronal : { '$in' : " + subDelList.toString() + " }, "
        		+ "    numCicloAnual : '" + revisionYear + "', "
        		+ "'$or' : ["
        		+ "        { '$and' : ["
        		+ "           { cveEstadoRegistro : { '$in' : [10, 11] } },"
        		+ "           { '$or' : ["
        		+ "            { cveSituacionRegistro : 2 }"
        		+ "           ] }"        	
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "           { idOrigenAlta : { '$exists' : false}}, "
        		+ "           { cveOrigenArchivo: 'MN'}, "
        		+ "           { cveSituacionRegistro : 1 },"
        		+ "           { cveEstadoRegistro : { '$in' : [10, 11] } }"        	
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "            { cveEstadoRegistro : { '$in' : [4, 8] } },"
        		+ "            { cveSituacionRegistro : 2 }"
        		+ "        ] },"
        		+ "        { '$and' : ["
        		+ "            { cveEstadoRegistro : { '$in' : [1, 5, 4, 8] } },"
        		+ "            { cveOrigenArchivo : 'MN' },"
        		+ "            { cveSituacionRegistro : 3 },"
        		+ "            {"
        		+ "                \"idOrigenAlta\":{$exists:false} "
        		+ "            }"
        		+ "        ] },"
        		+ "				 {"
        		+ "         \"$and\":["
        		+ "            {"
        		+ "               \"cveEstadoRegistro\":{"
        		+ "                  \"$in\":["
        		+ "                     1,5,4,8"
        		+ "                  ]"
        		+ "               }"
        		+ "            },"
        		+ "            {"
        		+ "               \"cveSituacionRegistro\":{$in:[ 2]}"
        		+ "            },"
        		+ "            {"
        		+ "               \"fecBaja\":{$exists:false}"
        		+ "            }"
        		+ "         ]}"
        		+ "    ]    "
        		+ "}";
    }
    
	public static String buildChangesDescartadosAnteriorJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
		int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
		return "{ " 
				+ "    numCicloAnual : { '$lt' : '" + revisionYear + "' },  " 
				+ "    cveDelRegPatronal : " + del + ", "
				+ "    cveSubDelRegPatronal : { '$in' : " + subDelList.toString() + " },"
				+ "	    \"$or\":[ "
				+ "      { "
				+ "         \"$and\":[ "
				+ "            { "
				+ "               \"cveOrigenArchivo\":{ "
				+ "                  \"$eq\":\"MN\" "
				+ "               } "
				+ "            }, "
				+ "            { "
				+ "               \"cveEstadoRegistro\":{ "
				+ "                  \"$in\":[ "
				+ "                     10, "
				+ "                     11 "
				+ "                  ] "
				+ "               } "
				+ "            }, "
				+ "            { "
				+ "               \"idOrigenAlta\":{ "
				+ "                  \"$exists\":false "
				+ "               } "
				+ "            } "
				+ "         ] "
				+ "      }, "
				+ "      { "
				+ "         \"$and\":[ "
				+ "            { "
				+ "               \"fecBaja\":{ "
				+ "                  \"$exists\":false "
				+ "               } "
				+ "            } "
				+ "         ] "
				+ "      } "
				+ "   ]" 
				+ " }";
	}

	public static String buildChangesDescartadosPosteriorJSONQuery(Integer del, List<Integer> subDelList, boolean isReprocess) {
		int revisionYear = Integer.parseInt(DateUtils.getCurrentYear()) - 1;
		return "{ " 
		+ "    numCicloAnual : { '$gt' : '" + revisionYear + "' },  " 
		+ "    cveDelRegPatronal : " + del + ", "
		+ "    cveSubDelRegPatronal : { '$in' : " + subDelList.toString() + " },"
		+ "	    \"$or\":[ "
		+ "      { "
		+ "         \"$and\":[ "
		+ "            { "
		+ "               \"cveOrigenArchivo\":{ "
		+ "                  \"$eq\":\"MN\" "
		+ "               } "
		+ "            }, "
		+ "            { "
		+ "               \"cveEstadoRegistro\":{ "
		+ "                  \"$in\":[ "
		+ "                     10, "
		+ "                     11 "
		+ "                  ] "
		+ "               } "
		+ "            }, "
		+ "            { "
		+ "               \"idOrigenAlta\":{ "
		+ "                  \"$exists\":false "
		+ "               } "
		+ "            } "
		+ "         ] "
		+ "      }, "
		+ "      { "
		+ "         \"$and\":[ "
		+ "            { "
		+ "               \"fecBaja\":{ "
		+ "                  \"$exists\":false "
		+ "               } "
		+ "            } "
		+ "         ] "
		+ "      } "
		+ "   ]" 
		+ " }";
	}

    public static TypedAggregation<MovementDTO> buildMovementsCountAggregation(Integer del, List<Integer> subDelList, boolean isReprocess) {
        String currentDate = DateUtils.getCurrentMongoDateString(isReprocess);
        int anioRevision = Integer.parseInt(DateUtils.getCurrentYear())-1;
        MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
                Criteria.where(CamposAseguradoEnum.FEC_BAJA.getDesc()).is(null),
                Criteria.where(CamposAseguradoEnum.ESTADO_REGISTRO.getDesc()).in(
                        Stream.concat(
                                Stream.of(ConfrontaConstants.CRITERIA_ESTADO_REGISTRO_CORRECTO),
                                Stream.of(ConfrontaConstants.CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE)
                        ).toArray()
                ),
                new Criteria().orOperator(
                        Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(null),
                        Criteria.where(MovementFieldsEnum.IS_PENDING.getDesc()).is(Boolean.FALSE)
                ),
                Criteria.where(CamposIncapacidadEnum.CONSECUENCIA.getDesc()).in(ConfrontaConstants.CRITERIA_CONSECUENCIA),
                Criteria.where(CamposPatronEnum.CVE_DEL_PATRON.getDesc()).is(del),
                Criteria.where(CamposPatronEnum.CVE_SUBDEL_PATRON.getDesc()).in(subDelList),
                Criteria.where(CamposAseguradoEnum.CYCLE.getDesc()).is(String.valueOf(anioRevision))                
        ));
        String addFieldsYearJSON = "{ $addFields: { year: { $subtract: [" + DateUtils.getCurrentYear() + ", 1] } } }";
        CustomAggregationOperation addFieldsYear = new CustomAggregationOperation(addFieldsYearJSON);
        String addFieldsCycleJSON = "{$addFields: {numCicloAnual: {'$toInt': "+anioRevision+" }}}";
        CustomAggregationOperation addFieldsCycle = new CustomAggregationOperation(addFieldsCycleJSON);        //
        String matchYearsJSON = "{$match: {$expr: {$eq: [ '$year', '$numCicloAnual' ]}}}";
        CustomAggregationOperation matchYears = new CustomAggregationOperation(matchYearsJSON);
        String matchDateJSON = "{$match: { '" + CamposAseguradoEnum.FEC_ALTA.getDesc() + "': { $lte: ISODate('" +
                currentDate.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
        CountOperation countOperation = Aggregation.count().as("count");
        return Aggregation.newAggregation(MovementDTO.class, matchDate, matchOperation, addFieldsYear, addFieldsCycle, matchYears, countOperation);
    }

    public static TypedAggregation<ChangeDTO> buildChangesCountAggregation(Integer del, List<Integer> subDelList, boolean isReprocess) {
    	int anioRevision = Integer.parseInt(DateUtils.getCurrentYear())-1;
        String currentDate = DateUtils.getCurrentMongoDateString(isReprocess);
        MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
                Criteria.where(ChangeFieldsEnum.FEC_BAJA.getDesc()).is(null),
                Criteria.where(ChangeFieldsEnum.ESTADO_REGISTRO.getDesc()).in(
                        Stream.concat(
                                Stream.of(ConfrontaConstants.CRITERIA_ESTADO_REGISTRO_CORRECTO),
                                Stream.of(ConfrontaConstants.CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE)
                        ).toArray()
                ),
                new Criteria().orOperator(
                        Criteria.where(ChangeFieldsEnum.IS_PENDING.getDesc()).is(null),
                        Criteria.where(ChangeFieldsEnum.IS_PENDING.getDesc()).is(Boolean.FALSE)
                ),
                Criteria.where(ChangeFieldsEnum.CONSECUENCIA.getDesc()).in(ConfrontaConstants.CRITERIA_CONSECUENCIA),
                Criteria.where(ChangeFieldsEnum.ORIGEN_ARCHIVO.getDesc()).is(ConfrontaConstants.MANUAL),
                Criteria.where(ChangeFieldsEnum.SITUACION_REGISTRO.getDesc()).is(ConfrontaConstants.CRITERIA_SITUACION_REGISTRO),
                Criteria.where(ChangeFieldsEnum.DEL_PATRON.getDesc()).is(del),
                Criteria.where(ChangeFieldsEnum.SUB_DEL_PATRON.getDesc()).in(subDelList),
                Criteria.where(ChangeFieldsEnum.CYCLE.getDesc()).is(String.valueOf(anioRevision))
        ));
        String addFieldsYearJSON = "{ $addFields: { year: { $subtract: [" + DateUtils.getCurrentYear() + ", 1] } } }";
        CustomAggregationOperation addFieldsYear = new CustomAggregationOperation(addFieldsYearJSON);
        String addFieldsCycleJSON = "{$addFields: {cicloAnual: {'$toInt': "+anioRevision +" }}}";
        CustomAggregationOperation addFieldsCycle = new CustomAggregationOperation(addFieldsCycleJSON);
        String matchYearsJSON = "{$match: {$expr: {$eq: [ '$year', '$cicloAnual' ]}}}";
        CustomAggregationOperation matchYears = new CustomAggregationOperation(matchYearsJSON);
        String matchDateJSON = "{$match: { '" + ChangeFieldsEnum.FEC_ALTA.getDesc() + "': { $lte: ISODate('" +
                currentDate.replace("CDT", "Z") + "') }}}";
        CustomAggregationOperation matchDate = new CustomAggregationOperation(matchDateJSON);
        CountOperation countOperation = Aggregation.count().as("count");
        return Aggregation.newAggregation(ChangeDTO.class, matchDate, matchOperation, addFieldsYear, addFieldsCycle, matchYears, countOperation);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarCorrectos(Long key){
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_CORRECTOS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarCorrectosOtras(Long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_CORRECTOS_OTRAS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarSusceptibles(Long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_SUSCEPTIBLES),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);    	
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarSusceptiblesOtras(Long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_SUSCEPTIBLES_OTRAS),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);    	
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarDefunciones(Long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_CONSECUENCIA.getDesc()).is(ConfrontaConstants.CVE_CONSECUENCIA_DEFUNCION),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarRegsEnviados(){
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> sumarPorcentajes(Long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, porcentajeIncapacidad : { $sum : '$porcentajeIncapacidad' } } }");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, sum);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> sumarDias(long key) {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.KEY.getDesc()).is(key)
    	));
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, diasSubsidiados : { $sum : '$diasSubsidiados' } } }");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, sum);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> totalRiesgos() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarTotalDefunciones() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_CONSECUENCIA.getDesc()).is(ConfrontaConstants.CVE_CONSECUENCIA_DEFUNCION)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> sumarTotalPorcentajes() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1)
    	));
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, porcentajeIncapacidad : { $sum : '$porcentajeIncapacidad' } } }");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, sum);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> sumarTotalDias() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1)
    	));
    	
    	CustomAggregationOperation sum = new CustomAggregationOperation("{ $group : { _id : null, diasSubsidiados : { $sum : '$diasSubsidiados' } } }");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, sum);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarTotalCorrectos(){
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_CORRECTOS)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarTotalCorrectosOtras() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_CORRECTOS_OTRAS)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarTotalSusceptibles() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_SUSCEPTIBLES)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);    	
    }
    
    public static TypedAggregation<BackupConfrontaDTO> contarTotalSusceptiblesOtras() {
    	MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(
    			Criteria.where(BackupFieldsEnum.ANIO_REVISION.getDesc()).is(Integer.parseInt(DateUtils.getCurrentYear()) - 1),
    			Criteria.where(BackupFieldsEnum.CVE_EDO_REG.getDesc()).is(ConfrontaConstants.CVE_SUSCEPTIBLES_OTRAS)
    	));
    	
    	CountOperation count = Aggregation.count().as("count");
    	
    	return Aggregation.newAggregation(BackupConfrontaDTO.class, matchOperation, count);    	
    }
    
}
