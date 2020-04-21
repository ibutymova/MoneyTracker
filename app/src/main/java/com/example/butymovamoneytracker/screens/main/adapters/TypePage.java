package com.example.butymovamoneytracker.screens.main.adapters;

public enum TypePage {
    PAGE_EXPENSE(0),
    PAGE_INCOME(1),
    PAGE_BALANCE(2);

    final int typeValue;

    TypePage(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public static TypePage getType(int typeValue){
        for(TypePage typePage : TypePage.values()) {
            if (typePage.getTypeValue() == typeValue)
                return typePage;
        }
        return null;
    }
}
