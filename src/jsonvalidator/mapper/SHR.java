/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonvalidator.mapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author tedb19
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public  class SHR {
    public PATIENT_IDENTIFICATION pATIENT_IDENTIFICATION;
    public String VERSION;
    public NEXT_OF_KIN nEXT_OF_KIN[];
    public HIV_TEST hIV_TEST[];
    public IMMUNIZATION iMMUNIZATION[];
    public MERGE_PATIENT_INFORMATION mERGE_PATIENT_INFORMATION;
    public CARD_DETAILS cARD_DETAILS;

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

    public static  class PATIENT_IDENTIFICATION implements Diffable<PATIENT_IDENTIFICATION>{
        public EXTERNAL_PATIENT_ID eXTERNAL_PATIENT_ID;
        public  INTERNAL_PATIENT_ID iNTERNAL_PATIENT_ID[];
        public  PATIENT_NAME pATIENT_NAME;
        public String dATE_OF_BIRTH;
        public  String dATE_OF_BIRTH_PRECISION;
        public String sEX;
        public  String dEATH_DATE;
        public  String dEATH_INDICATOR;
        public  PATIENT_ADDRESS pATIENT_ADDRESS;
        public  String pHONE_NUMBER;
        public  String mARITAL_STATUS;
        public  MOTHER_DETAILS mOTHER_DETAILS;

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

        public void setiNTERNAL_PATIENT_ID(INTERNAL_PATIENT_ID[] iNTERNAL_PATIENT_ID) {
            this.iNTERNAL_PATIENT_ID = iNTERNAL_PATIENT_ID;
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

        @Override
        public DiffResult diff(PATIENT_IDENTIFICATION pid) {
            return new DiffBuilder(this, pid, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("First Name: ", this.pATIENT_NAME.fIRST_NAME, pid.pATIENT_NAME.fIRST_NAME)
                    .append("Middle Name: ", this.pATIENT_NAME.mIDDLE_NAME, pid.pATIENT_NAME.mIDDLE_NAME)
                    .append("Last Name: ", this.pATIENT_NAME.lAST_NAME, pid.pATIENT_NAME.lAST_NAME)
                    .append("DOB: ", this.dATE_OF_BIRTH, pid.dATE_OF_BIRTH)
                    .append("Sex: ", this.sEX, pid.sEX)
                    .append("Phone Number: ", this.pHONE_NUMBER, pid.pHONE_NUMBER)
                    .append("County: ", this.pATIENT_ADDRESS.pHYSICAL_ADDRESS.cOUNTY, pid.pATIENT_ADDRESS.pHYSICAL_ADDRESS.cOUNTY)
                    .append("Sub county: ", this.pATIENT_ADDRESS.pHYSICAL_ADDRESS.sUB_COUNTY, pid.pATIENT_ADDRESS.pHYSICAL_ADDRESS.sUB_COUNTY)
                    .append("Nearest Landmark: ", this.pATIENT_ADDRESS.pHYSICAL_ADDRESS.nEAREST_LANDMARK, pid.pATIENT_ADDRESS.pHYSICAL_ADDRESS.nEAREST_LANDMARK)
                    .append("Village: ", this.pATIENT_ADDRESS.pHYSICAL_ADDRESS.vILLAGE, pid.pATIENT_ADDRESS.pHYSICAL_ADDRESS.vILLAGE)
                    .append("Ward: ", this.pATIENT_ADDRESS.pHYSICAL_ADDRESS.wARD, pid.pATIENT_ADDRESS.pHYSICAL_ADDRESS.wARD)
                    .append("Postal Address: ", this.pATIENT_ADDRESS.pOSTAL_ADDRESS, pid.pATIENT_ADDRESS.pOSTAL_ADDRESS)
                    .append("Mother First Name: ", this.mOTHER_DETAILS.mOTHER_NAME.fIRST_NAME, pid.mOTHER_DETAILS.mOTHER_NAME.fIRST_NAME)
                    .append("Mother Middle Name: ", this.mOTHER_DETAILS.mOTHER_NAME.mIDDLE_NAME, pid.mOTHER_DETAILS.mOTHER_NAME.mIDDLE_NAME)
                    .append("Mother Last Name: ", this.mOTHER_DETAILS.mOTHER_NAME.lAST_NAME, pid.mOTHER_DETAILS.mOTHER_NAME.lAST_NAME)
                    .append("Assigning Authority: ", this.eXTERNAL_PATIENT_ID.aSSIGNING_AUTHORITY, pid.eXTERNAL_PATIENT_ID.aSSIGNING_AUTHORITY)
                    .append("Assigning Facility: ", this.eXTERNAL_PATIENT_ID.aSSIGNING_FACILITY, pid.eXTERNAL_PATIENT_ID.aSSIGNING_FACILITY)
                    .append("ID: ", this.eXTERNAL_PATIENT_ID.iD, pid.eXTERNAL_PATIENT_ID.iD)
                    .append("Identifier Type: ", this.eXTERNAL_PATIENT_ID.iDENTIFIER_TYPE, pid.eXTERNAL_PATIENT_ID.iDENTIFIER_TYPE)
                    .build();
        }

        public static  class EXTERNAL_PATIENT_ID implements Diffable<EXTERNAL_PATIENT_ID> {
            public  String iD;
            public  String iDENTIFIER_TYPE;
            public  String aSSIGNING_AUTHORITY;
            public  String aSSIGNING_FACILITY;

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

            @Override
            public DiffResult diff(EXTERNAL_PATIENT_ID ext) {
                return new DiffBuilder(this, ext, ToStringStyle.SHORT_PREFIX_STYLE)
                        .append("Assigning Authority: ", this.aSSIGNING_AUTHORITY, ext.aSSIGNING_AUTHORITY)
                        .append("Assigning Facility: ", this.aSSIGNING_FACILITY, ext.aSSIGNING_FACILITY)
                        .append("ID: ", this.iD, ext.iD)
                        .append("Identifier Type: ", this.iDENTIFIER_TYPE, ext.iDENTIFIER_TYPE)
                        .build();
            }
        }

        public static  class INTERNAL_PATIENT_ID {
            public  String iD;
            public  String iDENTIFIER_TYPE;
            public  String aSSIGNING_AUTHORITY;
            public  String aSSIGNING_FACILITY;

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

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof INTERNAL_PATIENT_ID) {
                    return ((INTERNAL_PATIENT_ID) obj).iDENTIFIER_TYPE.equals(iDENTIFIER_TYPE);
                }
                return false;
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 29 * hash + (this.iDENTIFIER_TYPE).hashCode();
                return hash;
            }
        }

        public static  class PATIENT_NAME {
            public  String fIRST_NAME;
            public  String mIDDLE_NAME;
            public  String lAST_NAME;

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

        public static  class PATIENT_ADDRESS {
            public  PHYSICAL_ADDRESS pHYSICAL_ADDRESS;
            public  String pOSTAL_ADDRESS;

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

            public static  class PHYSICAL_ADDRESS {
                public  String vILLAGE;
                public  String wARD;
                public  String sUB_COUNTY;
                public  String cOUNTY;
                public  String nEAREST_LANDMARK;

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

        public static  class MOTHER_DETAILS {
            public  MOTHER_NAME mOTHER_NAME;
            public  MOTHER_IDENTIFIER mOTHER_IDENTIFIER[];

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

            public static  class MOTHER_NAME {
                public  String fIRST_NAME;
                public  String mIDDLE_NAME;
                public  String lAST_NAME;

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

            public static  class MOTHER_IDENTIFIER {
                public  String iD;
                public  String iDENTIFIER_TYPE;
                public  String aSSIGNING_AUTHORITY;
                public  String aSSIGNING_FACILITY;

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

                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof MOTHER_IDENTIFIER) {
                        return ((MOTHER_IDENTIFIER) obj).iDENTIFIER_TYPE.equals(iDENTIFIER_TYPE);
                    }
                    return false;
                }

                @Override
                public int hashCode() {
                    int hash = 7;
                    hash = 29 * hash + (this.iDENTIFIER_TYPE).hashCode();
                    return hash;
                }
            }
        }
    }

    public static  class NEXT_OF_KIN {
        public  NOK_NAME nOK_NAME;
        public  String rELATIONSHIP;
        public  String aDDRESS;
        public  String pHONE_NUMBER;
        public  String sEX;
        public  String dATE_OF_BIRTH;
        public  String cONTACT_ROLE;

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

        public static  class NOK_NAME {
            public  String fIRST_NAME;
            public  String mIDDLE_NAME;
            public  String lAST_NAME;

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

    public static  class HIV_TEST implements Diffable<HIV_TEST> {
        public  String dATE;
        public  String rESULT;
        public  String tYPE;
        public  String fACILITY;
        public  String sTRATEGY;
        public  PROVIDER_DETAILS pROVIDER_DETAILS;

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

        public static boolean compare(HIV_TEST hivTest, HIV_TEST hivTest1) {
            if(hivTest.dATE.equals(hivTest1.dATE) && hivTest.fACILITY.equals(hivTest1.fACILITY) && hivTest.tYPE.equals(hivTest1.tYPE) && hivTest.rESULT.equals(hivTest1.rESULT)){
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HIV_TEST) {
                return ((HIV_TEST) obj).dATE.equals(dATE) && ((HIV_TEST) obj).fACILITY.equals(fACILITY) && ((HIV_TEST) obj).tYPE.equals(tYPE) && ((HIV_TEST) obj).rESULT.equals(rESULT) ;
            }
            return false;
        }
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.tYPE + this.fACILITY + this.dATE).hashCode();
            return hash;
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

        @Override
        public DiffResult diff(HIV_TEST hivTest) {
            return new DiffBuilder(this, hivTest, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("HIV Test Date: ", this.dATE, hivTest.dATE)
                    .append("HIV Test Facility: ", this.fACILITY, hivTest.fACILITY)
                    .append("HIV Test Type: ", this.tYPE, hivTest.tYPE)
                    .build();
        }

        public static  class PROVIDER_DETAILS {
            public  String nAME;
            public  String iD;

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

    public static  class IMMUNIZATION {
        public  String nAME;
        public  String dATE_ADMINISTERED;

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

        public static boolean compare(IMMUNIZATION immunization, IMMUNIZATION immunization1) {
            if(immunization.dATE_ADMINISTERED.equals(immunization1.dATE_ADMINISTERED) && immunization.nAME.equals(immunization1.nAME)){
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IMMUNIZATION) {
                return ((IMMUNIZATION) obj).nAME.equals(nAME) && ((IMMUNIZATION) obj).dATE_ADMINISTERED.equals(dATE_ADMINISTERED);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.nAME + this.dATE_ADMINISTERED).hashCode();
            return hash;
        }
    }

    public static  class MERGE_PATIENT_INFORMATION {
        public  PRIOR_INTERNAL_IDENTIFIERS pRIOR_INTERNAL_IDENTIFIERS[];

        @JsonProperty("PRIOR_INTERNAL_IDENTIFIERS")
        public PRIOR_INTERNAL_IDENTIFIERS[] getpRIOR_INTERNAL_IDENTIFIERS() {
            return pRIOR_INTERNAL_IDENTIFIERS;
        }

        @JsonCreator
        public MERGE_PATIENT_INFORMATION(@JsonProperty(value="PRIOR_INTERNAL_IDENTIFIERS", required = true) PRIOR_INTERNAL_IDENTIFIERS[] pRIOR_INTERNAL_IDENTIFIERS){
            this.pRIOR_INTERNAL_IDENTIFIERS = pRIOR_INTERNAL_IDENTIFIERS;
        }

        public static  class PRIOR_INTERNAL_IDENTIFIERS {
            public  String iD;
            public  String iDENTIFIER_TYPE;
            public  String aSSIGNING_AUTHORITY;
            public  String aSSIGNING_FACILITY;
            public  String rEPLACEMENT_REASON;

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

    public static class CARD_DETAILS implements Diffable<CARD_DETAILS> {
        public String sTATUS;
        public String rEASON;
        public String lAST_UPDATED;
        public String lAST_UPDATED_FACILITY;

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

        @Override
        public DiffResult diff(CARD_DETAILS card_details) {
            return new DiffBuilder(this, card_details, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("Card Status: ", this.sTATUS, card_details.sTATUS)
                    .append("Reason: ", this.rEASON, card_details.rEASON)
                    .append("Last Updated: ", this.lAST_UPDATED, card_details.lAST_UPDATED)
                    .append("Last Updated Facility: ", this.lAST_UPDATED_FACILITY, card_details.lAST_UPDATED_FACILITY)
                    .build();
        }
    }
}