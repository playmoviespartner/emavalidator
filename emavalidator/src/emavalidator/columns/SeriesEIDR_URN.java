package emavalidator.columns;

import emavalidator.AbstractColumnDefinition;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.errors.CellErrorSpecificValueFormat;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class SeriesEIDR_URN extends AbstractColumnDefinition
{

    @Override
    public void buildValidators()
    {
        this.validators.add(new CellValidatorRegexFormat(new String[]{
                ValidatorUtils.EIDR_FORMAT_REGEX,
                ValidatorUtils.EMPTY_STRING_REGEX},
                false,
                ErrorLevel.ERROR,
                CellErrorSpecificValueFormat.EIDR_FORMAT_ERROR,
                ValidatorUtils.EXPECTED_EIDR_VALUES));
    }

}
