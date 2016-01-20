package emavalidator.utils;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EMAXMLParser
{
    String fileName;
    
    public EMAXMLParser(String fileName) {
        this.fileName = fileName;
    }
    
    class MyXMLHandler extends DefaultHandler {
        
        boolean vIsOpen, isCountryType, isNumberType, isIdentifierType, isDisplayStringType;
        boolean isTerritory;
        boolean isEpisodeNumber, isSeasonNumber;
        boolean isEpisodeAltID, isSeasonAltID, isSeriesAltID;
        boolean isEpisodeMetadata, isSeasonMetadata, isSeriesMetadata;
        boolean isCompanyDisplayCredit;
        
        // Array of values holding all the contents of a row.
        String[] rowContents = new String[58];
        
        /*
        boolean isDisplayName, isStoreLanguage, isTerritory, isWorkType, isEntryType, 
                isSeriesTitleInternalAlias, isSeriesTitleDisplayUnlimited, isSeasonNumber, isEpisodeNumber,
                isLocalizationType, isEpisodeTitleInternalAlias, isEpisodeTitleDisplayUnlimited,
                isSeasonTitleInternalAlias, isSeasonTitleDisplayUnlimited, isEpisodeCount, isSeasonCount,
                isSeriesAltID, isSeasonAltID, isEpisodeAltID, isCompanyDisplayCredit, isLicenseType,
                isLicenseRightsDescription, isFormatProfile, isStart, isEnd, isSpecialPreOrderFulfillDate,
                isPriceType, isPriceValue, isSRP, isDescription, isOtherTerms, isOtherInstructions,
                isSeriesContentID, isSeasonContentID, isEpisodeContentID, isEpisodeProductID, isEncodeID,
                isAvailID, isMetadata, isSuppressionLiftDate, isReleaseYear, isReleaseHistoryOriginal,
                isReleaseHistoryPhysicalHV, isExceptionsFlag, isRatingSystem, isRatingValue, isRatingReason,
                isRentalDuration, isWatchDuration, isFixedEndDate, isCaptionIncluded, isCaptionException,
                isAny, isContractID, isServiceProvider, isTotalRunTime, isHoldbackLanguage, isHoldbackExclusionLanguage;
        */
        
        public MyXMLHandler() {
            
        }
        
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {
            
//            String msg = String.format("Name: %s, Attributes: %s", name, attributes.toString());
        
//            System.out.println("Start Element: " + msg);
            
            // get attributes.
//            int length = attributes.getLength();
//            for( int i = 0; i < length; i++ ) {
//                String n = attributes.getQName(i);
//                System.out.println(n);
//            }
            
            // <md:Number> tags used by EpisodeNumber and SeasonNumber
            if ( name.equals(EMAXMLTVConstants.MD_NUMBER) ) {
                if (isEpisodeNumber | isSeasonNumber) {
                    isNumberType = true;
                    vIsOpen = true;
                }
            } else if ( name.equals(EMAXMLTVConstants.MD_IDENTIFIER) ) {
                if (isEpisodeAltID || isSeasonAltID || isSeriesAltID) {
                    isIdentifierType = true;
                    vIsOpen = true;
                }
            } else if ( name.equals(EMAXMLTVConstants.MD_DISPLAY_STRING) ) {
                if (isCompanyDisplayCredit) {
                    isDisplayStringType = true;
                    vIsOpen = true;
                }
            } else if ( name.equals(EMAXMLTVConstants.MD_COUNTRY) ) {
                if (isTerritory) {
                    isCountryType = true;
                    vIsOpen = true;
                }
            } else if ( name.equals(EMAXMLTVConstants.TERM) ) {
                // get attributes.
                int length = attributes.getLength();
                for( int i = 0; i < length; i++ ) {
//                    String n = attributes.getQName(i);
                    String v = attributes.getValue(i);
//                    System.out.println(n + "=" + v);
                    // PriceType depends on specific termName terms
                    if ( v.equals(EMAXMLTVConstants.TERM_PRICE_TYPE_SEASONWSP) ) {
                        System.out.println(EMAXMLTVConstants.PRICE_TYPE_EXCEL);
                        System.out.println(v);
                    } else if ( v.equals(EMAXMLTVConstants.TERM_PRICE_TYPE_EPISODEWSP) ) {
                        System.out.println(EMAXMLTVConstants.PRICE_TYPE_EXCEL);
                        System.out.println(v);
                    } else if ( v.equals(EMAXMLTVConstants.SRP_XML) ) {
                        
                    } else if ( v.equals(EMAXMLTVConstants.HOLDBACK_LANGUAGE_XML) ) {
                        System.out.println(EMAXMLTVConstants.HOLDBACK_LANGUAGE_EXCEL);

                    } else if ( v.equals(EMAXMLTVConstants.HOLDBACK_EXCLUSION_LANGUAGE_XML) ) {
                        System.out.println(EMAXMLTVConstants.HOLDBACK_EXCLUSION_LANGUAGE_EXCEL);

                    } else if ( v.equals(EMAXMLTVConstants.RENTAL_DURATION_XML) ) {
                        
                    } else if ( v.equals(EMAXMLTVConstants.WATCH_DURATION_XML) ) {
                        
                    } else if ( v.equals(EMAXMLTVConstants.FIXED_END_DATE_XML) ) {
                        
                    }
                }
            }
            
            if ( name.equals(EMAXMLTVConstants.DISPLAY_NAME_XML) ) {
                System.out.println(EMAXMLTVConstants.DISPLAY_NAME_EXCEL);
                vIsOpen = true;
            } else if ( name.equals(EMAXMLTVConstants.STORE_LANGUAGE_XML) ) {
                System.out.println(EMAXMLTVConstants.STORE_LANGUAGE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.TERRITORY_XML) ) {
                System.out.println(EMAXMLTVConstants.TERRITORY_EXCEL);
                isTerritory = true;

            } else if ( name.equals(EMAXMLTVConstants.WORKTYPE_XML) ) {
                System.out.println(EMAXMLTVConstants.WORKTYPE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.ENTRY_TYPE_XML) ) {
                System.out.println(EMAXMLTVConstants.ENTRY_TYPE_EXCEL);
                vIsOpen = true;
            } else if ( name.equals(EMAXMLTVConstants.SERIES_TITLE_INTERNAL_ALIAS_XML) ) {
                System.out.println(EMAXMLTVConstants.SERIES_TITLE_INTERNAL_ALIAS_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_TITLE_DISPLAY_UNLIMITED_XML) ) {
                System.out.println(EMAXMLTVConstants.SERIES_TITLE_DISPLAY_UNLIMITED_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_NUMBER_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_NUMBER_EXCEL);
                isSeasonNumber = true;
            } else if ( name.equals(EMAXMLTVConstants.EPISODE_NUMBER_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_NUMBER_EXCEL);
                isEpisodeNumber = true;
            } else if ( name.equals(EMAXMLTVConstants.LOCALIZATION_TYPE_XML) ) {
                System.out.println(EMAXMLTVConstants.LOCALIZATION_TYPE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_TITLE_INTERNAL_ALIAS_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_TITLE_INTERNAL_ALIAS_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_TITLE_DISPLAY_UNLIMITED_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_TITLE_DISPLAY_UNLIMITED_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_TITLE_INTERNAL_ALIAS_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_TITLE_INTERNAL_ALIAS_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_TITLE_DISPLAY_UNLIMITED_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_TITLE_DISPLAY_UNLIMITED_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_COUNT_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_COUNT_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_COUNT_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_COUNT_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_ALT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.SERIES_ALT_ID_EXCEL);
                isSeriesAltID = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_ALT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_ALT_ID_EXCEL);
                isSeasonAltID = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_ALT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_ALT_ID_EXCEL);
//                vIsOpen = true;
                isEpisodeAltID = true;
            } 

            
            else if ( name.equals(EMAXMLTVConstants.COMPANY_DISPLAY_CREDIT_XML) ) {
                System.out.println(EMAXMLTVConstants.COMPANY_DISPLAY_CREDIT_EXCEL);
                isCompanyDisplayCredit = true;
            } else if ( name.equals(EMAXMLTVConstants.LICENSE_TYPE_XML) ) {
                System.out.println(EMAXMLTVConstants.LICENSE_TYPE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.LICENSE_RIGHTS_DESCRIPTION_XML) ) {
                System.out.println(EMAXMLTVConstants.LICENSE_RIGHTS_DESCRIPTION_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.FORMAT_PROFILE_XML) ) {
                System.out.println(EMAXMLTVConstants.FORMAT_PROFILE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.START_XML) ) {
                System.out.println(EMAXMLTVConstants.START_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.END_XML) ) {
                System.out.println(EMAXMLTVConstants.END_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SPECIAL_PREORDER_FULFILL_DATE_XML) ) {
                System.out.println(EMAXMLTVConstants.SPECIAL_PREORDER_FULFILL_DATE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.PRICE_TYPE_XML) ) {
                System.out.println(EMAXMLTVConstants.PRICE_TYPE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.PRICE_VALUE_XML) ) {
                System.out.println(EMAXMLTVConstants.PRICE_VALUE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SRP_XML) ) {
                System.out.println(EMAXMLTVConstants.SRP_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.DESCRIPTION_XML) ) {
                System.out.println(EMAXMLTVConstants.DESCRIPTION_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.OTHER_TERMS_XML) ) {
                System.out.println(EMAXMLTVConstants.OTHER_TERMS_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.OTHER_INSTRUCTIONS_XML) ) {
                System.out.println(EMAXMLTVConstants.OTHER_INSTRUCTIONS_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_CONTENT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.SERIES_CONTENT_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_CONTENT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.SEASON_CONTENT_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_CONTENT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_CONTENT_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_PRODUCT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.EPISODE_PRODUCT_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.ENCODE_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.ENCODE_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.AVAIL_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.AVAIL_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.METADATA_XML) ) {
                System.out.println(EMAXMLTVConstants.METADATA_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SUPPRESSION_LIFT_DATE_XML) ) {
                System.out.println(EMAXMLTVConstants.SUPPRESSION_LIFT_DATE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_YEAR_XML) ) {
                System.out.println(EMAXMLTVConstants.RELEASE_YEAR_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_HISTORY_ORIGINAL_XML) ) {
                System.out.println(EMAXMLTVConstants.RELEASE_HISTORY_ORIGINAL_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_HISTORY_PHYSICAL_HV_XML) ) {
                System.out.println(EMAXMLTVConstants.RELEASE_HISTORY_PHYSICAL_HV_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.EXCEPTIONS_FLAG_XML) ) {
                System.out.println(EMAXMLTVConstants.EXCEPTIONS_FLAG_EXCEL);

            } else if ( name.equals(EMAXMLTVConstants.RATING_SYSTEM_XML) ) {
                System.out.println(EMAXMLTVConstants.RATING_SYSTEM_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RATING_VALUE_XML) ) {
                System.out.println(EMAXMLTVConstants.RATING_VALUE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RATING_REASON_XML) ) {
                System.out.println(EMAXMLTVConstants.RATING_REASON_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.RENTAL_DURATION_XML) ) {
                System.out.println(EMAXMLTVConstants.RENTAL_DURATION_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.WATCH_DURATION_XML) ) {
                System.out.println(EMAXMLTVConstants.WATCH_DURATION_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.FIXED_END_DATE_XML) ) {
                System.out.println(EMAXMLTVConstants.FIXED_END_DATE_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.CAPTION_INCLUDED_XML) ) {
                System.out.println(EMAXMLTVConstants.CAPTION_INCLUDED_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.CAPTION_EXEMPTION_XML) ) {
                System.out.println(EMAXMLTVConstants.CAPTION_EXEMPTION_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.ANY_XML) ) {
                System.out.println(EMAXMLTVConstants.ANY_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.CONTRACT_ID_XML) ) {
                System.out.println(EMAXMLTVConstants.CONTRACT_ID_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.SERVICE_PROVIDER_XML) ) {
                System.out.println(EMAXMLTVConstants.SERVICE_PROVIDER_EXCEL);
                vIsOpen = true;

            } else if ( name.equals(EMAXMLTVConstants.TOTAL_RUN_TIME_XML) ) {
                System.out.println(EMAXMLTVConstants.TOTAL_RUN_TIME_EXCEL);
                vIsOpen = true;

            }
        } // end startElement()
        
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if ( name.equals(EMAXMLTVConstants.MD_NUMBER) ) {
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.MD_IDENTIFIER) ) {
                isIdentifierType = false;
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.MD_DISPLAY_STRING) ) {
                if (isCompanyDisplayCredit) {
                    isDisplayStringType = false;
                }
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.MD_COUNTRY) ) {
                isCountryType = false;
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.TERM) ) {
                vIsOpen = false;
            }
            
            if ( name.equals(EMAXMLTVConstants.DISPLAY_NAME_XML) ) {
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.STORE_LANGUAGE_XML) ) {
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.TERRITORY_XML) ) {
                isTerritory = false;

            } else if ( name.equals(EMAXMLTVConstants.WORKTYPE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.ENTRY_TYPE_XML) ) {
                vIsOpen = false;
            } else if ( name.equals(EMAXMLTVConstants.SERIES_TITLE_INTERNAL_ALIAS_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_TITLE_DISPLAY_UNLIMITED_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_NUMBER_XML) ) {
                isSeasonNumber = false;
            } else if ( name.equals(EMAXMLTVConstants.EPISODE_NUMBER_XML) ) {
                isEpisodeNumber = false;

            } else if ( name.equals(EMAXMLTVConstants.LOCALIZATION_TYPE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_TITLE_INTERNAL_ALIAS_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_TITLE_DISPLAY_UNLIMITED_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_TITLE_INTERNAL_ALIAS_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_TITLE_DISPLAY_UNLIMITED_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_COUNT_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_COUNT_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_ALT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_ALT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_ALT_ID_XML) ) {
                isEpisodeAltID = false;

            } else if ( name.equals(EMAXMLTVConstants.COMPANY_DISPLAY_CREDIT_XML) ) {
                isCompanyDisplayCredit = true;
                
            } else if ( name.equals(EMAXMLTVConstants.LICENSE_TYPE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.LICENSE_RIGHTS_DESCRIPTION_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.FORMAT_PROFILE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.START_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.END_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SPECIAL_PREORDER_FULFILL_DATE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.PRICE_TYPE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.PRICE_VALUE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SRP_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.DESCRIPTION_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.OTHER_TERMS_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.OTHER_INSTRUCTIONS_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SERIES_CONTENT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SEASON_CONTENT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_CONTENT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EPISODE_PRODUCT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.ENCODE_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.AVAIL_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.METADATA_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SUPPRESSION_LIFT_DATE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_YEAR_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_HISTORY_ORIGINAL_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RELEASE_HISTORY_PHYSICAL_HV_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.EXCEPTIONS_FLAG_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RATING_SYSTEM_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RATING_VALUE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RATING_REASON_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.RENTAL_DURATION_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.WATCH_DURATION_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.FIXED_END_DATE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.CAPTION_INCLUDED_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.CAPTION_EXEMPTION_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.ANY_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.CONTRACT_ID_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.SERVICE_PROVIDER_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.TOTAL_RUN_TIME_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.HOLDBACK_LANGUAGE_XML) ) {
                vIsOpen = false;

            } else if ( name.equals(EMAXMLTVConstants.HOLDBACK_EXCLUSION_LANGUAGE_XML) ) {
                vIsOpen = false;

            } 
        } // end endElement()
        
        public void characters(char ch[], int start, int length) throws SAXException {
//            String msg = String.format("ch: %s, start %d, length %d", new String(ch, start, length), start, length);
            String value = new String(ch, start, length);
            
            // Get value of <EpisodeNumber><md:Number>
            if (vIsOpen && isEpisodeNumber && isNumberType) {
                System.out.println(value);
            }
            // Get value of <SeasonNumber><md:Number>
            else if (vIsOpen && isSeasonNumber && isNumberType) {
                System.out.println(value);
            }
            // Get value of <AltIdentifier><md:Identifier>
            else if (vIsOpen && isEpisodeAltID && isIdentifierType) {
                System.out.println(value);
            }
            // Get all other values from a <Tag>
            else if (vIsOpen) {
                System.out.println(value);
            }
            
            
            /*
            if (isDisplayName) {
                System.out.println(value);
            } else if (isStoreLanguage) {
                
            } else if (isTerritory) {
                
            } else if (isWorkType) {
                
            } else if (isEntryType) {
                
            } else if (isSeriesTitleInternalAlias) {
                
            } else if (isSeriesTitleDisplayUnlimited) {
                
            } else if (isSeasonNumber) {
                
            } else if (isEpisodeNumber) {
                
            } else if (isLocalizationType) {
                
            } else if (isEpisodeTitleInternalAlias) {
                
            } else if (isEpisodeTitleDisplayUnlimited) {
                
            } else if (isSeasonTitleDisplayUnlimited) {
                
            } else if (isEpisodeCount) {
                
            } else if (isSeasonCount) {
                
            } else if (isSeriesAltID) {
                
            } else if (isSeasonAltID) {
                
            } else if (isEpisodeAltID) {
                
            } else if (isCompanyDisplayCredit) {
                
            } else if (isLicenseType) {
                
            } else if (isLicenseRightsDescription) {
                
            } else if (isFormatProfile) {
                
            } else if (isStart) {
                
            } else if (isEnd) {
                
            } else if (isSpecialPreOrderFulfillDate) {
                
            } else if (isPriceType) {
                
            } else if (isPriceValue) {
                
            } else if (isSRP) {
                
            } else if (isDescription) {
                
            } else if (isOtherTerms) {
                
            } else if (isOtherInstructions) {
                
            } else if (isSeriesContentID) {
                
            } else if (isSeasonContentID) {
                
            } else if (isEpisodeContentID) {
                
            } else if (isEpisodeProductID) {
                
            } else if (isEncodeID) {
                
            } else if (isAvailID) {
                
            } else if (isMetadata) {
                
            } else if (isSuppressionLiftDate) {
                
            } else if (isReleaseYear) {
                
            } else if (isReleaseHistoryOriginal) {
                
            } else if (isReleaseHistoryPhysicalHV) {
                
            } else if (isExceptionsFlag) {
                
            } else if (isRatingSystem) {
                
            } else if (isRatingValue) {
                
            } else if (isRatingReason) {
                
            } else if (isRentalDuration) {
                
            } else if (isWatchDuration) {
                
            } else if (isFixedEndDate) {
                
            } else if (isCaptionIncluded) {
                
            } else if (isCaptionException) {
                
            } else if (isAny) {
                
            } else if (isContractID) {
                
            } else if (isServiceProvider) {
                
            } else if (isTotalRunTime) {
                
            } else if (isHoldbackLanguage) {
                
            } else if (isHoldbackExclusionLanguage) {
                
            } else {
                
            }*/

        } // end characters()

    }
    
    public void process() throws ParserConfigurationException, SAXException {
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
        try {
            SAXParser parser = factory.newSAXParser();

            parser.parse(this.fileName, new MyXMLHandler());            
        } catch (SAXException se) {
            
        } catch (IOException ioe) {
            
        }

    }
}
