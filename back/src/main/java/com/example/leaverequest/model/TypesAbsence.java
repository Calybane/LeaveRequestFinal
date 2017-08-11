package com.example.leaverequest.model;

public class TypesAbsence
{
    public static String ANNUAL = "Annual leave";
    public static String SPECIAL = "Special leave";
    
    public static String[] typesAbsence()
    {
        return new String[] {TypesAbsence.ANNUAL, TypesAbsence.SPECIAL};
    }
}
