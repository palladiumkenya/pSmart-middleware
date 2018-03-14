/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.mapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 *
 * @author tedb19
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public final class SHR {
    public final PATIENT_IDENTIFICATION pATIENT_IDENTIFICATION;
    public final String VERSION;
    public final NEXT_OF_KIN nEXT_OF_KIN[];
    public final HIV_TEST hIV_TEST[];
    public final IMMUNIZATION iMMUNIZATION[];
    public final MERGE_PATIENT_INFORMATION mERGE_PATIENT_INFORMATION;
    public final CARD_DETAILS cARD_DETAILS;

    @JsonProperty("PATIENT_IDENTIFICATION")
    public PATIENT_IDENTIFICATION getpATIENT_IDENTIFICATION() {
        return pATIENT_IDENTIFICATION;
    }

    @JsonProperty("NEXT_OF_KIN")
    public NEXT_OF_KIN[] getnEXT_OF_KIN() {
        return nEXT_OF_KIN;
    }

    @JsonProperty("HIV_TEST")
    public HIV_TEST[] gethIV_TEST() {
        return hIV_TEST;
    }

    @JsonProperty("IMMUNIZATION")
    public IMMUNIZATION[] getiMMUNIZATION() {
        return iMMUNIZATION;
    }

    @JsonProperty("MERGE_PATIENT_INFORMATION")
    public MERGE_PATIENT_INFORMATION getmERGE_PATIENT_INFORMATION() {
        return mERGE_PATIENT_INFORMATION;
    }

    @JsonProperty("CARD_DETAILS")
    public CARD_DETAILS getcARD_DETAILS() {
        return cARD_DETAILS;
    }

    @JsonProperty("VERSION")
    public String getVERSION() {
        return VERSION;
    }

    @JsonCreator
    public SHR(@JsonProperty("VERSION") String VERSION, @JsonProperty("PATIENT_IDENTIFICATION") PATIENT_IDENTIFICATION pATIENT_IDENTIFICATION, @JsonProperty("NEXT_OF_KIN") NEXT_OF_KIN[] nEXT_OF_KIN, @JsonProperty("HIV_TEST") HIV_TEST[] hIV_TEST, @JsonProperty("IMMUNIZATION") IMMUNIZATION[] iMMUNIZATION, @JsonProperty("MERGE_PATIENT_INFORMATION") MERGE_PATIENT_INFORMATION mERGE_PATIENT_INFORMATION, @JsonProperty("CARD_DETAILS") CARD_DETAILS cARD_DETAILS){
        this.pATIENT_IDENTIFICATION = pATIENT_IDENTIFICATION;
        this.VERSION = VERSION;
        this.nEXT_OF_KIN = nEXT_OF_KIN;
        this.hIV_TEST = hIV_TEST;
        this.iMMUNIZATION = iMMUNIZATION;
        this.mERGE_PATIENT_INFORMATION = mERGE_PATIENT_INFORMATION;
        this.cARD_DETAILS = cARD_DETAILS;
    }

    public static final class PATIENT_IDENTIFICATION {
        public final EXTERNAL_PATIENT_ID eXTERNAL_PATIENT_ID;
        public final INTERNAL_PATIENT_ID iNTERNAL_PATIENT_ID[];
        public final PATIENT_NAME pATIENT_NAME;
        public final String dATE_OF_BIRTH;
        public final String dATE_OF_BIRTH_PRECISION;
        public final String sEX;
        public final String dEATH_DATE;
        public final String dEATH_INDICATOR;
        public final PATIENT_ADDRESS pATIENT_ADDRESS;
        public final String pHONE_NUMBER;
        public final String mARITAL_STATUS;
        public final MOTHER_DETAILS mOTHER_DETAILS;

        @JsonCreator
        public PATIENT_IDENTIFICATION(@JsonProperty("EXTERNAL_PATIENT_ID") EXTERNAL_PATIENT_ID eXTERNAL_PATIENT_ID, @JsonProperty("INTERNAL_PATIENT_ID") INTERNAL_PATIENT_ID[] iNTERNAL_PATIENT_ID, @JsonProperty("PATIENT_NAME") PATIENT_NAME pATIENT_NAME, @JsonProperty("DATE_OF_BIRTH") String dATE_OF_BIRTH, @JsonProperty("DATE_OF_BIRTH_PRECISION") String dATE_OF_BIRTH_PRECISION, @JsonProperty("SEX") String sEX, @JsonProperty("DEATH_DATE") String dEATH_DATE, @JsonProperty("DEATH_INDICATOR") String dEATH_INDICATOR, @JsonProperty("PATIENT_ADDRESS") PATIENT_ADDRESS pATIENT_ADDRESS, @JsonProperty("PHONE_NUMBER") String pHONE_NUMBER, @JsonProperty("MARITAL_STATUS") String mARITAL_STATUS, @JsonProperty("MOTHER_DETAILS") MOTHER_DETAILS mOTHER_DETAILS){
            this.eXTERNAL_PATIENT_ID = eXTERNAL_PATIENT_ID;
            this.iNTERNAL_PATIENT_ID = iNTERNAL_PATIENT_ID;
            this.pATIENT_NAME = pATIENT_NAME;
            this.dATE_OF_BIRTH = dATE_OF_BIRTH;
            this.dATE_OF_BIRTH_PRECISION = dATE_OF_BIRTH_PRECISION;
            this.sEX = sEX;
            this.dEATH_DATE = dEATH_DATE;
            this.dEATH_INDICATOR = dEATH_INDICATOR;
            this.pATIENT_ADDRESS = pATIENT_ADDRESS;
            this.pHONE_NUMBER = pHONE_NUMBER;
            this.mARITAL_STATUS = mARITAL_STATUS;
            this.mOTHER_DETAILS = mOTHER_DETAILS;
        }

        @JsonProperty("EXTERNAL_PATIENT_ID")
        public EXTERNAL_PATIENT_ID geteXTERNAL_PATIENT_ID() {
            return eXTERNAL_PATIENT_ID;
        }

        @JsonProperty("INTERNAL_PATIENT_ID")
        public INTERNAL_PATIENT_ID[] getiNTERNAL_PATIENT_ID() {
            return iNTERNAL_PATIENT_ID;
        }

        @JsonProperty("PATIENT_NAME")
        public PATIENT_NAME getpATIENT_NAME() {
            return pATIENT_NAME;
        }

        @JsonProperty("DATE_OF_BIRTH")
        public String getdATE_OF_BIRTH() {
            return dATE_OF_BIRTH;
        }

        @JsonProperty("DATE_OF_BIRTH_PRECISION")
        public String getdATE_OF_BIRTH_PRECISION() {
            return dATE_OF_BIRTH_PRECISION;
        }

        @JsonProperty("SEX")
        public String getsEX() {
            return sEX;
        }

        @JsonProperty("DEATH_DATE")
        public String getdEATH_DATE() {
            return dEATH_DATE;
        }

        @JsonProperty("DEATH_INDICATOR")
        public String getdEATH_INDICATOR() {
            return dEATH_INDICATOR;
        }

        @JsonProperty("PATIENT_ADDRESS")
        public PATIENT_ADDRESS getpATIENT_ADDRESS() {
            return pATIENT_ADDRESS;
        }

        @JsonProperty("PHONE_NUMBER")
        public String getpHONE_NUMBER() {
            return pHONE_NUMBER;
        }

        @JsonProperty("MARITAL_STATUS")
        public String getmARITAL_STATUS() {
            return mARITAL_STATUS;
        }
        @JsonProperty("MOTHER_DETAILS")
        public MOTHER_DETAILS getmOTHER_DETAILS() {
            return mOTHER_DETAILS;
        }

        public static final class EXTERNAL_PATIENT_ID {
            public final String iD;
            public final String iDENTIFIER_TYPE;
            public final String aSSIGNING_AUTHORITY;
            public final String aSSIGNING_FACILITY;

            @JsonProperty("ID")
            public String getiD() {
                return iD;
            }

            @JsonProperty("IDENTIFIER_TYPE")
            public String getiDENTIFIER_TYPE() {
                return iDENTIFIER_TYPE;
            }

            @JsonProperty("ASSIGNING_AUTHORITY")
            public String getaSSIGNING_AUTHORITY() {
                return aSSIGNING_AUTHORITY;
            }

            @JsonProperty("ASSIGNING_FACILITY")
            public String getaSSIGNING_FACILITY() {
                return aSSIGNING_FACILITY;
            }

            @JsonCreator
            public EXTERNAL_PATIENT_ID(@JsonProperty("ID") String iD, @JsonProperty("IDENTIFIER_TYPE") String iDENTIFIER_TYPE, @JsonProperty("ASSIGNING_AUTHORITY") String aSSIGNING_AUTHORITY, @JsonProperty("ASSIGNING_FACILITY") String aSSIGNING_FACILITY){
                this.iD = iD;
                this.iDENTIFIER_TYPE = iDENTIFIER_TYPE;
                this.aSSIGNING_AUTHORITY = aSSIGNING_AUTHORITY;
                this.aSSIGNING_FACILITY = aSSIGNING_FACILITY;
            }
        }

        public static final class INTERNAL_PATIENT_ID {
            public final String iD;
            public final String iDENTIFIER_TYPE;
            public final String aSSIGNING_AUTHORITY;
            public final String aSSIGNING_FACILITY;

            @JsonProperty("ID")
            public String getiD() {
                return iD;
            }
            @JsonProperty("IDENTIFIER_TYPE")
            public String getiDENTIFIER_TYPE() {
                return iDENTIFIER_TYPE;
            }

            @JsonProperty("ASSIGNING_AUTHORITY")
            public String getaSSIGNING_AUTHORITY() {
                return aSSIGNING_AUTHORITY;
            }

            @JsonProperty("ASSIGNING_FACILITY")
            public String getaSSIGNING_FACILITY() {
                return aSSIGNING_FACILITY;
            }

            @JsonCreator
            public INTERNAL_PATIENT_ID(@JsonProperty("ID") String iD, @JsonProperty("IDENTIFIER_TYPE") String iDENTIFIER_TYPE, @JsonProperty("ASSIGNING_AUTHORITY") String aSSIGNING_AUTHORITY, @JsonProperty("ASSIGNING_FACILITY") String aSSIGNING_FACILITY){
                this.iD = iD;
                this.iDENTIFIER_TYPE = iDENTIFIER_TYPE;
                this.aSSIGNING_AUTHORITY = aSSIGNING_AUTHORITY;
                this.aSSIGNING_FACILITY = aSSIGNING_FACILITY;
            }
        }

        public static final class PATIENT_NAME {
            public final String fIRST_NAME;
            public final String mIDDLE_NAME;
            public final String lAST_NAME;

            @JsonProperty("FIRST_NAME")
            public String getfIRST_NAME() {
                return fIRST_NAME;
            }

            @JsonProperty("MIDDLE_NAME")
            public String getmIDDLE_NAME() {
                return mIDDLE_NAME;
            }

            @JsonProperty("LAST_NAME")
            public String getlAST_NAME() {
                return lAST_NAME;
            }

            @JsonCreator
            public PATIENT_NAME(@JsonProperty("FIRST_NAME") String fIRST_NAME, @JsonProperty("MIDDLE_NAME") String mIDDLE_NAME, @JsonProperty("LAST_NAME") String lAST_NAME){
                this.fIRST_NAME = fIRST_NAME;
                this.mIDDLE_NAME = mIDDLE_NAME;
                this.lAST_NAME = lAST_NAME;
            }
        }

        public static final class PATIENT_ADDRESS {
            public final PHYSICAL_ADDRESS pHYSICAL_ADDRESS;
            public final String pOSTAL_ADDRESS;

            @JsonProperty("PHYSICAL_ADDRESS")
            public PHYSICAL_ADDRESS getpHYSICAL_ADDRESS() {
                return pHYSICAL_ADDRESS;
            }

            @JsonProperty("POSTAL_ADDRESS")
            public String getpOSTAL_ADDRESS() {
                return pOSTAL_ADDRESS;
            }

            @JsonCreator
            public PATIENT_ADDRESS(@JsonProperty("PHYSICAL_ADDRESS") PHYSICAL_ADDRESS pHYSICAL_ADDRESS, @JsonProperty("POSTAL_ADDRESS") String pOSTAL_ADDRESS){
                this.pHYSICAL_ADDRESS = pHYSICAL_ADDRESS;
                this.pOSTAL_ADDRESS = pOSTAL_ADDRESS;
            }
    
            public static final class PHYSICAL_ADDRESS {
                public final String vILLAGE;
                public final String wARD;
                public final String sUB_COUNTY;
                public final String cOUNTY;
                public final String nEAREST_LANDMARK;

                @JsonProperty("VILLAGE")
                public String getvILLAGE() {
                    return vILLAGE;
                }

                @JsonProperty("WARD")
                public String getwARD() {
                    return wARD;
                }

                @JsonProperty("SUB_COUNTY")
                public String getsUB_COUNTY() {
                    return sUB_COUNTY;
                }

                @JsonProperty("COUNTY")
                public String getcOUNTY() {
                    return cOUNTY;
                }

                @JsonProperty("NEAREST_LANDMARK")
                public String getnEAREST_LANDMARK() {
                    return nEAREST_LANDMARK;
                }

                @JsonCreator
                public PHYSICAL_ADDRESS(@JsonProperty("VILLAGE") String vILLAGE, @JsonProperty("WARD") String wARD, @JsonProperty("SUB_COUNTY") String sUB_COUNTY, @JsonProperty("COUNTY") String cOUNTY, @JsonProperty("NEAREST_LANDMARK") String nEAREST_LANDMARK){
                    this.vILLAGE = vILLAGE;
                    this.wARD = wARD;
                    this.sUB_COUNTY = sUB_COUNTY;
                    this.cOUNTY = cOUNTY;
                    this.nEAREST_LANDMARK = nEAREST_LANDMARK;
                }
            }
        }

        public static final class MOTHER_DETAILS {
            public final MOTHER_NAME mOTHER_NAME;
            public final MOTHER_IDENTIFIER mOTHER_IDENTIFIER[];

            @JsonProperty("MOTHER_NAME")
            public MOTHER_NAME getmOTHER_NAME() {
                return mOTHER_NAME;
            }

            @JsonProperty("MOTHER_IDENTIFIER")
            public MOTHER_IDENTIFIER[] getmOTHER_IDENTIFIER() {
                return mOTHER_IDENTIFIER;
            }

            @JsonCreator
            public MOTHER_DETAILS(@JsonProperty("MOTHER_NAME") MOTHER_NAME mOTHER_NAME, @JsonProperty("MOTHER_IDENTIFIER") MOTHER_IDENTIFIER[] mOTHER_IDENTIFIER){
                this.mOTHER_NAME = mOTHER_NAME;
                this.mOTHER_IDENTIFIER = mOTHER_IDENTIFIER;
            }
    
            public static final class MOTHER_NAME {
                public final String fIRST_NAME;
                public final String mIDDLE_NAME;
                public final String lAST_NAME;

                @JsonProperty("FIRST_NAME")
                public String getfIRST_NAME() {
                    return fIRST_NAME;
                }
                @JsonProperty("MIDDLE_NAME")
                public String getmIDDLE_NAME() {
                    return mIDDLE_NAME;
                }
                @JsonProperty("LAST_NAME")
                public String getlAST_NAME() {
                    return lAST_NAME;
                }

                @JsonCreator
                public MOTHER_NAME(@JsonProperty("FIRST_NAME") String fIRST_NAME, @JsonProperty("MIDDLE_NAME") String mIDDLE_NAME, @JsonProperty("LAST_NAME") String lAST_NAME){
                    this.fIRST_NAME = fIRST_NAME;
                    this.mIDDLE_NAME = mIDDLE_NAME;
                    this.lAST_NAME = lAST_NAME;
                }
            }
    
            public static final class MOTHER_IDENTIFIER {
                public final String iD;
                public final String iDENTIFIER_TYPE;
                public final String aSSIGNING_AUTHORITY;
                public final String aSSIGNING_FACILITY;

                @JsonProperty("ID")
                public String getiD() {
                    return iD;
                }

                @JsonProperty("IDENTIFIER_TYPE")
                public String getiDENTIFIER_TYPE() {
                    return iDENTIFIER_TYPE;
                }
                @JsonProperty("ASSIGNING_AUTHORITY")
                public String getaSSIGNING_AUTHORITY() {
                    return aSSIGNING_AUTHORITY;
                }
                @JsonProperty("ASSIGNING_FACILITY")
                public String getaSSIGNING_FACILITY() {
                    return aSSIGNING_FACILITY;
                }

                @JsonCreator
                public MOTHER_IDENTIFIER(@JsonProperty("ID") String iD, @JsonProperty("IDENTIFIER_TYPE") String iDENTIFIER_TYPE, @JsonProperty("ASSIGNING_AUTHORITY") String aSSIGNING_AUTHORITY, @JsonProperty("ASSIGNING_FACILITY") String aSSIGNING_FACILITY){
                    this.iD = iD;
                    this.iDENTIFIER_TYPE = iDENTIFIER_TYPE;
                    this.aSSIGNING_AUTHORITY = aSSIGNING_AUTHORITY;
                    this.aSSIGNING_FACILITY = aSSIGNING_FACILITY;
                }
            }
        }
    }

    public static final class NEXT_OF_KIN {
        public final NOK_NAME nOK_NAME;
        public final String rELATIONSHIP;
        public final String aDDRESS;
        public final String pHONE_NUMBER;
        public final String sEX;
        public final String dATE_OF_BIRTH;
        public final String cONTACT_ROLE;

        @JsonProperty("NOK_NAME")
        public NOK_NAME getnOK_NAME() {
            return nOK_NAME;
        }
        @JsonProperty("RELATIONSHIP")
        public String getrELATIONSHIP() {
            return rELATIONSHIP;
        }
        @JsonProperty("ADDRESS")
        public String getaDDRESS() {
            return aDDRESS;
        }
        @JsonProperty("PHONE_NUMBER")
        public String getpHONE_NUMBER() {
            return pHONE_NUMBER;
        }
        @JsonProperty("SEX")
        public String getsEX() {
            return sEX;
        }
        @JsonProperty("DATE_OF_BIRTH")
        public String getdATE_OF_BIRTH() {
            return dATE_OF_BIRTH;
        }
        @JsonProperty("CONTACT_ROLE")
        public String getcONTACT_ROLE() {
            return cONTACT_ROLE;
        }

        @JsonCreator
        public NEXT_OF_KIN(@JsonProperty("NOK_NAME") NOK_NAME nOK_NAME, @JsonProperty("RELATIONSHIP") String rELATIONSHIP, @JsonProperty("ADDRESS") String aDDRESS, @JsonProperty("PHONE_NUMBER") String pHONE_NUMBER, @JsonProperty("SEX") String sEX, @JsonProperty("DATE_OF_BIRTH") String dATE_OF_BIRTH, @JsonProperty("CONTACT_ROLE") String cONTACT_ROLE){
            this.nOK_NAME = nOK_NAME;
            this.rELATIONSHIP = rELATIONSHIP;
            this.aDDRESS = aDDRESS;
            this.pHONE_NUMBER = pHONE_NUMBER;
            this.sEX = sEX;
            this.dATE_OF_BIRTH = dATE_OF_BIRTH;
            this.cONTACT_ROLE = cONTACT_ROLE;
        }

        public static final class NOK_NAME {
            public final String fIRST_NAME;
            public final String mIDDLE_NAME;
            public final String lAST_NAME;

            @JsonProperty("FIRST_NAME")
            public String getfIRST_NAME() {
                return fIRST_NAME;
            }

            @JsonProperty("MIDDLE_NAME")
            public String getmIDDLE_NAME() {
                return mIDDLE_NAME;
            }

            @JsonProperty("LAST_NAME")
            public String getlAST_NAME() {
                return lAST_NAME;
            }

            @JsonCreator
            public NOK_NAME(@JsonProperty("FIRST_NAME") String fIRST_NAME, @JsonProperty("MIDDLE_NAME") String mIDDLE_NAME, @JsonProperty("LAST_NAME") String lAST_NAME){
                this.fIRST_NAME = fIRST_NAME;
                this.mIDDLE_NAME = mIDDLE_NAME;
                this.lAST_NAME = lAST_NAME;
            }
        }
    }

    public static final class HIV_TEST {
        public final String dATE;
        public final String rESULT;
        public final String tYPE;
        public final String fACILITY;
        public final String sTRATEGY;
        public final PROVIDER_DETAILS pROVIDER_DETAILS;

        @JsonProperty("DATE")
        public String getdATE() {
            return dATE;
        }
        @JsonProperty("RESULT")
        public String getrESULT() {
            return rESULT;
        }
        @JsonProperty("TYPE")
        public String gettYPE() {
            return tYPE;
        }
        @JsonProperty("FACILITY")
        public String getfACILITY() {
            return fACILITY;
        }
        @JsonProperty("STRATEGY")
        public String getsTRATEGY() {
            return sTRATEGY;
        }
        @JsonProperty("PROVIDER_DETAILS")
        public PROVIDER_DETAILS getpROVIDER_DETAILS() {
            return pROVIDER_DETAILS;
        }

        @JsonCreator
        public HIV_TEST(@JsonProperty("DATE") String dATE, @JsonProperty("RESULT") String rESULT, @JsonProperty("TYPE") String tYPE, @JsonProperty("FACILITY") String fACILITY, @JsonProperty("STRATEGY") String sTRATEGY, @JsonProperty("PROVIDER_DETAILS") PROVIDER_DETAILS pROVIDER_DETAILS){
            this.dATE = dATE;
            this.rESULT = rESULT;
            this.tYPE = tYPE;
            this.fACILITY = fACILITY;
            this.sTRATEGY = sTRATEGY;
            this.pROVIDER_DETAILS = pROVIDER_DETAILS;
        }

        public static final class PROVIDER_DETAILS {
            public final String nAME;
            public final String iD;

            @JsonProperty("NAME")
            public String getnAME() {
                return nAME;
            }

            @JsonProperty("ID")
            public String getiD() {
                return iD;
            }

            @JsonCreator
            public PROVIDER_DETAILS(@JsonProperty("NAME") String nAME, @JsonProperty("ID") String iD){
                this.nAME = nAME;
                this.iD = iD;
            }
        }
    }

    public static final class IMMUNIZATION {
        public final String nAME;
        public final String dATE_ADMINISTERED;

        @JsonProperty("NAME")
        public String getnAME() {
            return nAME;
        }
        @JsonProperty("DATE_ADMINISTERED")
        public String getdATE_ADMINISTERED() {
            return dATE_ADMINISTERED;
        }

        @JsonCreator
        public IMMUNIZATION(@JsonProperty("NAME") String nAME, @JsonProperty("DATE_ADMINISTERED") String dATE_ADMINISTERED){
            this.nAME = nAME;
            this.dATE_ADMINISTERED = dATE_ADMINISTERED;
        }
    }

    public static final class MERGE_PATIENT_INFORMATION {
        public final PRIOR_INTERNAL_IDENTIFIERS pRIOR_INTERNAL_IDENTIFIERS[];

        @JsonProperty("PRIOR_INTERNAL_IDENTIFIERS")
        public PRIOR_INTERNAL_IDENTIFIERS[] getpRIOR_INTERNAL_IDENTIFIERS() {
            return pRIOR_INTERNAL_IDENTIFIERS;
        }

        @JsonCreator
        public MERGE_PATIENT_INFORMATION(@JsonProperty(value="PRIOR_INTERNAL_IDENTIFIERS", required = true) PRIOR_INTERNAL_IDENTIFIERS[] pRIOR_INTERNAL_IDENTIFIERS){
            this.pRIOR_INTERNAL_IDENTIFIERS = pRIOR_INTERNAL_IDENTIFIERS;
        }

        public static final class PRIOR_INTERNAL_IDENTIFIERS {
            public final String iD;
            public final String iDENTIFIER_TYPE;
            public final String aSSIGNING_AUTHORITY;
            public final String aSSIGNING_FACILITY;
            public final String rEPLACEMENT_REASON;

            @JsonProperty("ID")
            public String getiD() {
                return iD;
            }

            @JsonProperty("IDENTIFIER_TYPE")
            public String getiDENTIFIER_TYPE() {
                return iDENTIFIER_TYPE;
            }
            @JsonProperty("ASSIGNING_AUTHORITY")
            public String getaSSIGNING_AUTHORITY() {
                return aSSIGNING_AUTHORITY;
            }
            @JsonProperty("ASSIGNING_FACILITY")
            public String getaSSIGNING_FACILITY() {
                return aSSIGNING_FACILITY;
            }
            @JsonProperty("REPLACEMENT_REASON")
            public String getrEPLACEMENT_REASON() {
                return rEPLACEMENT_REASON;
            }

            @JsonCreator
            public PRIOR_INTERNAL_IDENTIFIERS(@JsonProperty("ID") String iD, @JsonProperty("IDENTIFIER_TYPE") String iDENTIFIER_TYPE, @JsonProperty("ASSIGNING_AUTHORITY") String aSSIGNING_AUTHORITY, @JsonProperty("ASSIGNING_FACILITY") String aSSIGNING_FACILITY, @JsonProperty("REPLACEMENT_REASON") String rEPLACEMENT_REASON){
                this.iD = iD;
                this.iDENTIFIER_TYPE = iDENTIFIER_TYPE;
                this.aSSIGNING_AUTHORITY = aSSIGNING_AUTHORITY;
                this.aSSIGNING_FACILITY = aSSIGNING_FACILITY;
                this.rEPLACEMENT_REASON = rEPLACEMENT_REASON;
            }
        }
    }

    public static final class CARD_DETAILS {
        public final String sTATUS;
        public final String rEASON;
        public final String lAST_UPDATED;
        public final String lAST_UPDATED_FACILITY;

        @JsonProperty("STATUS")
        public String getsTATUS() {
            return sTATUS;
        }
        @JsonProperty("REASON")
        public String getrEASON() {
            return rEASON;
        }
        @JsonProperty("LAST_UPDATED")
        public String getlAST_UPDATED() {
            return lAST_UPDATED;
        }
        @JsonProperty("LAST_UPDATED_FACILITY")
        public String getlAST_UPDATED_FACILITY() {
            return lAST_UPDATED_FACILITY;
        }

        @JsonCreator
        public CARD_DETAILS(@JsonProperty(value="STATUS", required = true) String sTATUS, @JsonProperty("REASON") String rEASON, @JsonProperty("LAST_UPDATED") String lAST_UPDATED, @JsonProperty("LAST_UPDATED_FACILITY") String lAST_UPDATED_FACILITY){
            this.sTATUS = sTATUS;
            this.rEASON = rEASON;
            this.lAST_UPDATED = lAST_UPDATED;
            this.lAST_UPDATED_FACILITY = lAST_UPDATED_FACILITY;
        }
    }
}