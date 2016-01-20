package emavalidator.columns;

import emavalidator.AbstractColumnDefinition;
import emavalidator.errors.CellErrorSpecialSymbols;
import emavalidator.validators.CellValidatorSpecialSymbols;
import emavalidator.validators.ValidatorUtils;

public class CollectionAltID extends AbstractColumnDefinition
{

    @Override
    public void buildValidators()
    {
        this.validators.add(new CellValidatorSpecialSymbols(
                ValidatorUtils.ILLEGAL_METADATA_CHARACTERS,
                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS,
                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS_EXPECTED));
    }

}
