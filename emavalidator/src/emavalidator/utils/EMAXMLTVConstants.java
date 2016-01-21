package emavalidator.utils;

public class EMAXMLTVConstants
{
    // MD Types
    public static String MD_COUNTRY = "md:country";
    public static String MD_NUMBER = "md:Number";
    public static String MD_IDENTIFIER = "md:Identifier";
    public static String MD_DISPLAY_STRING = "md:DisplayString";
    
    // XML Specific Tags
    public static String TRANSACTION = "Transaction";
    public static String EPISODE_METADATA = "EpisodeMetadata";
    public static String SEASON_METADATA = "SeasonMetadata";
    public static String SERIES_METADATA = "SeriesMetadata";
    public static String TERM = "Term";
    
    // TermNames
    public static String TERM_PRICE_TYPE_EPISODEWSP = "EpisodeWSP";
    public static String TERM_PRICE_TYPE_SEASONWSP = "SeasonWSP";
    public static String TERM_SRP = "SRP";
    public static String TERM_HOLDBACK_LANGUAGE = "HoldbackLanguage";
    public static String TERM_HOLDBACK_EXCLUSION_LANGUAGE = "HoldbackExclusionLanguage";
    
    // Col A: DisplayName
    //  //Licensor/DisplayName
    public static String DISPLAY_NAME_EXCEL = "DisplayName";
    public static String DISPLAY_NAME_XML = "md:DisplayName";
    
    // Col B: StoreLanguage - Language or languages to which transaction applies. If absent, then all languages is assumed.
    //  //Transaction/StoreLanguage
    public static String STORE_LANGUAGE_EXCEL = "StoreLanguage";
    public static String STORE_LANGUAGE_XML = "StoreLanguage";
    
    // Col C: Territory
    //  //Transaction/Territory
    public static String TERRITORY_EXCEL = "Territory";
    public static String TERRITORY_XML = "md:country";
    
    // Col D: WorkType
    //  //Asset/WorkType
    public static String WORKTYPE_EXCEL = "WorkType";
    public static String WORKTYPE_XML = "WorkType";
    
    // Col E: EntryType
    //  //Disposition/EntryType
    public static String ENTRY_TYPE_EXCEL = "EntryType";
    public static String ENTRY_TYPE_XML = "EntryType";

    // Col F: SeriesTitleInternalAlias
    //  For season: //Asset/SeasonMetadata/SeriesMetadata/SeriesTitleInternalAlias
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeriesMetadata/SeriesTitleInternalAlias
    public static String SERIES_TITLE_INTERNAL_ALIAS_EXCEL = "SeriesTitleInternalAlias";
    public static String SERIES_TITLE_INTERNAL_ALIAS_XML = "SeriesTitleInternalAlias";    
    
    // Col G: SeriesTitleDisplayUnlimited
    //  For season: //Asset/SeasonMetadata/SeriesMetadata/SeriesTitleDisplayUnlimited
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeriesMetadata/SeriesTitleDisplayUnlimited
    public static String SERIES_TITLE_DISPLAY_UNLIMITED_EXCEL = "SeriesTitleDisplayUnlimited";
    public static String SERIES_TITLE_DISPLAY_UNLIMITED_XML = "SeriesTitleDisplayUnlimited";
    
    // Col H: SeasonNumber
    //  For season: //Asset/SeasonMetadata/SeasonNumber/Number
    //  For episode: //Asset/EpisodeMetadata/SeasonNumber/Number
    public static String SEASON_NUMBER_EXCEL = "SeasonNumber";
    public static String SEASON_NUMBER_XML = "SeasonNumber";
    
    // Col I: EpisodeNumber
    //  //Asset/EpisodeMetadata/EpisodeNumber/Number
    public static String EPISODE_NUMBER_EXCEL = "EpisodeNumber";
    public static String EPISODE_NUMBER_XML = "EpisodeNumber";
    
    // Col J: LocalizationType
    //  //Asset/EpisodeMetadata/LocalizationOffering
    public static String LOCALIZATION_TYPE_EXCEL = "LocalizationType";
    public static String LOCALIZATION_TYPE_XML = "LocalizationOffering";

    // Col K: TitleInternalAlias
    //  //Asset/EpisodeMetadata/EpisodeTitleInternalAlias
    public static String EPISODE_TITLE_INTERNAL_ALIAS_EXCEL = "EpisodeTitleInternalAlias";
    public static String EPISODE_TITLE_INTERNAL_ALIAS_XML = "TitleInternalAlias";
    
    // Col L: TitleDisplayUnlimited
    //  //Asset/EpisodeMetadata/EpisodeTitleDisplayUnlimited
    public static String EPISODE_TITLE_DISPLAY_UNLIMITED_EXCEL = "EpisodeTitleDisplayUnlimited";
    public static String EPISODE_TITLE_DISPLAY_UNLIMITED_XML = "TitleDisplayUnlimited";
    
    // Col M: TitleInternalAlias
    //  For season: //Asset/SeasonMetadata/SeasonTitleInternalAlias
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeasonTitleInternalAlias
    public static String SEASON_TITLE_INTERNAL_ALIAS_EXCEL = "SeasonTitleInternalAlias";
    public static String SEASON_TITLE_INTERNAL_ALIAS_XML = "SeasonTitleInternalAlias";
    
    // Col N: SeasonTitleDisplayUnlimited
    //  For season: //Asset/SeasonMetadata/SeasonTitleDisplayUnlimited
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeasonTitleDisplayUnlimited
    public static String SEASON_TITLE_DISPLAY_UNLIMITED_EXCEL = "SeasonTitleDisplayUnlimited";
    public static String SEASON_TITLE_DISPLAY_UNLIMITED_XML = "SeasonTitleDisplayUnlimited";
    
    // EpisodeCount
    //  For season: //Asset/SeasonMetadata/NumberOfEpisodes
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/NumberOfEpisodes
    public static String EPISODE_COUNT_EXCEL = "EpisodeCount";
    public static String EPISODE_COUNT_XML = "NumberOfEpisodes";
    
    // SeasonCount
    //  For season: //Asset/SeasonMetadata/SeriesMetadata/NumberOfSeasons
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeriesMetadata/NumberOfSeasons
    public static String SEASON_COUNT_EXCEL = "SeasonCount";
    public static String SEASON_COUNT_XML = "NumberOfSeasons";
    
    // SeriesAltID
    //  For season: //Asset/SeasonMetadata/SeriesMetadata/SeriesAltIdentifier
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeriesMetadata/SeriesAltIdentifier
    public static String SERIES_ALT_ID_EXCEL = "SeriesAltID";
    public static String SERIES_ALT_ID_XML = "SeriesAltIdentifier";
    
    // SeasonAltID
    //  For season: //Asset/SeasonMetadata/SeasonAltIdentifier
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeasonAltIdentifier
    public static String SEASON_ALT_ID_EXCEL = "SeasonAltID";
    public static String SEASON_ALT_ID_XML = "SeasonAltIdentifier";
    
    // EpisodeAltID
    //  //Asset/EpisodeMetadata/EpisodeAltIdentifier
    public static String EPISODE_ALT_ID_EXCEL = "EpisodeAltID";
    public static String EPISODE_ALT_ID_XML = "AltIdentifier"; // todo(ckha): check if it's EpisodeAltIdentifier??
    
    // Col T: CompanyDisplayCredit - md:DisplayString
    //  For season: //Asset/SeasonMetadata/CompanyDisplayCredit
    //  For episode: //Asset/EpisodeMetadata/CompanyDisplayCredit
    public static String COMPANY_DISPLAY_CREDIT_EXCEL = "CompanyDisplayCredit";
    public static String COMPANY_DISPLAY_CREDIT_XML = "CompanyDisplayCredit";
    
    // Col Q: LicenseType
    //  //Transaction/LicenseType
    public static String LICENSE_TYPE_EXCEL = "LicenseType";
    public static String LICENSE_TYPE_XML = "LicenseType";
    
    // Col R: LicenseRightsDescription
    //  //Transaction/LicenseRightsDescription
    public static String LICENSE_RIGHTS_DESCRIPTION_EXCEL = "LicenseRightsDescription";
    public static String LICENSE_RIGHTS_DESCRIPTION_XML = "LicenseRightsDescription";
    
    // Col S: FormatProfile
    //  //Transaction/FormatProfile
    public static String FORMAT_PROFILE_EXCEL = "FormatProfile";
    public static String FORMAT_PROFILE_XML = "FormatProfile";

    // Col T: Start
    //  //Transaction/Start
    public static String START_EXCEL = "Start";
    public static String START_XML = "Start";

    // Col U: End
    //  //Transaction/End
    public static String END_EXCEL = "End";
    public static String END_XML = "End";

    // SpecialPreOrderFulfillDate
    //  //Transaction/Term@termName="PreOrderFulfillDate"
    //  //Transaction/Term/Event
    public static String SPECIAL_PREORDER_FULFILL_DATE_EXCEL = "SpecialPreOrderFulfillDate";
    public static String SPECIAL_PREORDER_FULFILL_DATE_XML = "PreOrderFulfillDate";
    
    // Col W: PriceType | XML: attribute:termName
    //  For Tier, Category, SRP: //Transaction/Term@termName
    //  For episode WSP: //Transaction/Term@termName="EpisodeWSP"
    //  For Season WSP: //Transaction/Term@termName="SeasonWSP"
    public static String PRICE_TYPE_EXCEL = "PriceType";
    public static String PRICE_TYPE_XML = "SeasonWSP";

    // Col X: PriceValue | XML: tag Money
    //  For Tier, Category: //Transaction/Term/Text
    //  For WSP, SRP: //Transaction/Term/Money
    public static String PRICE_VALUE_EXCEL = "PriceValue";
    public static String PRICE_VALUE_XML = "Money";

    // SRP 
    //  For Tier, Category, SRP: //Transaction/Term@termName
    //  For episode WSP: //Transaction/Term@termName="EpisodeWSP"
    //  For Season WSP: //Transaction/Term@termName="SeasonWSP"
    public static String SRP_EXCEL = "SRP";
    public static String SRP_XML = "SRP";

    // Col Z: Description
    //  //Transaction/Description
    public static String DESCRIPTION_EXCEL = "Description";
    public static String DESCRIPTION_XML = "Description";

    // OtherTerms Not found
    //  
    public static String OTHER_TERMS_EXCEL = "OtherTerms";
    public static String OTHER_TERMS_XML = "";
    
    // OtherInstructions Not found
    //  //Transaction/OtherInstructions
    public static String OTHER_INSTRUCTIONS_EXCEL = "OtherInstructions";
    public static String OTHER_INSTRUCTIONS_XML = "";

    // Col AG: SeriesContentID
    //  For season: //Asset/SeasonMetadata/SeriesMetadata/SeriesContentID
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeriesMetadata/SeriesContentID
    public static String SERIES_CONTENT_ID_EXCEL = "SeriesContentID";
    public static String SERIES_CONTENT_ID_XML = "SeriesContentID";

    // Col AH: SeasonContentID
    //  For season: //Asset/SeasonMetadata/SeasonContentID
    //  For season: //Asset@contentID
    //  For episode: //Asset/EpisodeMetadata/SeasonMetadata/SeasonContentID
    public static String SEASON_CONTENT_ID_EXCEL = "SeasonContentID";
    public static String SEASON_CONTENT_ID_XML = "SeasonContentID";
    
    // Col AD: EpisodeContentID | within Asset tag, attribute:contentID
    //  //Asset@contentID
    public static String EPISODE_CONTENT_ID_EXCEL = "EpisodeContentID";
    public static String EPISODE_CONTENT_ID_XML = "contentID"; //attribute
    
    // Col AJ: EpisodeProductID | XML: ProductID or ALID tag
    //  //Asset/EpisodeMetadata/ProductID
    public static String EPISODE_PRODUCT_ID_EXCEL = "EpisodeProductID";
    public static String EPISODE_PRODUCT_ID_XML = "ProductID"; // ALID

    // todo(ckha):not found?
    public static String ENCODE_ID_EXCEL = "EncodeID";
    public static String ENCODE_ID_XML = "";
    
    // Col AH: AvailID | In Avail tag: attribute:AvailID
    //  //@AvailID
    public static String AVAIL_ID_EXCEL = "AvailID";
    public static String AVAIL_ID_XML = "AvailID"; //attribute

    // todo(ckha):not found?
    public static String METADATA_EXCEL = "Metadata";
    public static String METADATA_XML = "Metadata";
    
    // SuppressionLiftDate
    // Transaction/Term@termName="AnnounceDate"
    // Transaction/Term/Event
    public static String SUPPRESSION_LIFT_DATE_EXCEL = "SuppressionLiftDate";
    public static String SUPPRESSION_LIFT_DATE_XML = "Event"; // Term@termName="AnnounceDate"
    
    // ReleaseYear
    //  For season: //Asset/SeasonMetadata/ReleaseDate
    //  For episode: //Asset/EpisodeMetadata/ReleaseDate
    public static String RELEASE_YEAR_EXCEL = "ReleaseYear";
    public static String RELEASE_YEAR_XML = "ReleaseDate";

    // ReleaseHistoryOriginal
    //  For season: //Asset/SeasonMetadata/ReleaseHistory/Date (ReleaseType = 'original')
    //  For episode: //Asset/EpisodeMetadata/ReleaseHistory/Date (ReleaseType = 'original')
    public static String RELEASE_HISTORY_ORIGINAL_EXCEL = "ReleaseHistoryOriginal";
    public static String RELEASE_HISTORY_ORIGINAL_XML = "Date"; // ReleaseType original
    
    //  For season: //Asset/SeasonMetadata/ReleaseHistory/Date (ReleaseType = 'DVD')
    //  For episode: //Asset/EpisodeMetadata/ReleaseHistory/Date (ReleaseType = 'DVD')
    public static String RELEASE_HISTORY_PHYSICAL_HV_EXCEL = "ReleaseHistoryPhysicalHV";
    public static String RELEASE_HISTORY_PHYSICAL_HV_XML = "Date"; // ReleaseType DVD

    // ExceptionsFlag
    //  //ExceptionsFlag
    public static String EXCEPTIONS_FLAG_EXCEL = "ExceptionsFlag";
    public static String EXCEPTIONS_FLAG_XML = "ExceptionsFlag";

    // RatingSystem
    //  For season: //Asset/SeasonMetadata/Ratings/Rating/System
    //  For episode: //Asset/EpisodeMetadata/Ratings/Rating/System
    public static String RATING_SYSTEM_EXCEL = "RatingSystem";
    public static String RATING_SYSTEM_XML = "System";

    // RatingValue
    //  For season: //Asset/SeasonMetadata/Ratings/Rating/Value
    //  For episode: //Asset/EpisodeMetadata/Ratings/Rating/Value
    public static String RATING_VALUE_EXCEL = "RatingValue";
    public static String RATING_VALUE_XML = "Value";

    // RatingReason
    //  For season: //Asset/SeasonMetadata/Ratings/Rating/Reason (split into separate elements)
    //  For episode: //Asset/EpisodeMetadata/Ratings/Rating/Reason (split into separate elements)
    public static String RATING_REASON_EXCEL = "RatingReason";
    public static String RATING_REASON_XML = "Reason";

    // RentalDuration
    //  //Transaction/Term@termName="RentalDuration"
    //  //Transaction/Term/Duration
    public static String RENTAL_DURATION_EXCEL = "RentalDuration";
    public static String RENTAL_DURATION_XML = "RentalDuration"; // attribute:RentalDuration
    
    // WatchDuration
    //  //Transaction/Term@termName="WatchDuration"
    //  //Transaction/Term/Duration
    public static String WATCH_DURATION_EXCEL = "WatchDuration";
    public static String WATCH_DURATION_XML = "WatchDuration"; // attribute:WatchDuration
    
    // RentalDuration
    //  //Transaction/Term@termName="FixedEndDate"
    //  //Transaction/Term/Event
    public static String FIXED_END_DATE_EXCEL = "FixedEndDate";
    public static String FIXED_END_DATE_XML = "FixedEndDate";

    // CaptionIncluded
    // todo(ckha): not found?
    public static String CAPTION_INCLUDED_EXCEL = "CaptionIncluded";
    public static String CAPTION_INCLUDED_XML = "";
    
    // CaptionExemption
    //  //Asset/EpisodeMetadata/USACaptionsExemptionReason
    public static String CAPTION_EXEMPTION_EXCEL = "CaptionExemption";
    public static String CAPTION_EXEMPTION_XML = "USACaptionsExemptionReason";

    // Any
    // todo(ckha): not found
    public static String ANY_EXCEL = "Any";
    public static String ANY_XML = "";
    
    // ContractID
    //  //Transaction/ContractID
    public static String CONTRACT_ID_EXCEL = "ContractID";
    public static String CONTRACT_ID_XML = "ContractID";

    // ServiceProvider
    //  //ServiceProvider
    public static String SERVICE_PROVIDER_EXCEL = "ServiceProvider";
    public static String SERVICE_PROVIDER_XML = "ServiceProvider";

    // TotalRunTime
    //  //Asset/EpisodeMetadata/RunLength
    public static String TOTAL_RUN_TIME_EXCEL = "TotalRunTime";
    public static String TOTAL_RUN_TIME_XML = "RunLength";

    // HoldbackLanguage
    //  //Transaction/Term@termName="HoldbackLanguage"
    //  //Transaction/Term/Language
    public static String HOLDBACK_LANGUAGE_EXCEL = "HoldbackLanguage";
    public static String HOLDBACK_LANGUAGE_XML = "HoldbackLanguage"; //attribute

    // HoldbackExclusionLanguage
    //  //Transaction/Term@termName="HoldbackExclusionLanguage"
    //  //Transaction/Term/Language
    public static String HOLDBACK_EXCLUSION_LANGUAGE_EXCEL = "HoldbackExclusionLanguage";
    public static String HOLDBACK_EXCLUSION_LANGUAGE_XML = "HoldbackExclusionLanguage"; //attribute

    
}
