/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Piter
 */
public class Nazwy {
    public static List<String> Imiona = Arrays.asList("Jan", "Jakub", "Andrzej", "Baltazar", "Piotr", "Marek", "Zdzisław");
    public static List<String> Nazwiska = Arrays.asList("Kowalski", "Nowak", "Drapieżny-Ślimak", "Zwycięzca", "Kopytko", "Sobczak");
    public static List<String> SurowceNazwy = new LinkedList<>(Arrays.asList("Miedz", "Złoto", "Srebro", "Uran", "Cynk", "Ropa", "Pallad", "Platyna", "Nikiel", "Aluminium", "Benzyna", "Gaz ziemny", "Bawełna"));
    public static List<String> SurowceJednostki = new LinkedList<>(Arrays.asList("tona", "uncja", "uncja", "mg", "tona", "baryłka", "uncja", "uncja", "tona", "tona", "galon", "mln btu", "funt"));
    public static List<String> SpolkiNazwa1 = Arrays.asList("Central", "NorthWest", "Southwest", "Global", "Best", "Unknown", "4FUN", "KoniecPomysłów", "American", "EU", "Unique", "China");
    public static List<String> SpolkiNazwa2 = Arrays.asList("Construction", "Enterprise", "Company", "United", "Pharmaceutics", "Investments", "Motors", "Incorporation", "Corporation", "Mobile", "Racing");
    public static List<String> Spolki = new LinkedList<>();
    public static List<String> Waluty = new LinkedList<>(Arrays.asList("GBP", "EUR", "USD", "CHF", "SEK", "AUD", "CAD", "CZK", "RUB", "JPY", "CNY"));
    public static List<String> Kraj = new LinkedList<>(Arrays.asList("Wielka Brytania", "Strefa Euro", "USA", "Szwajcaria", "Szwecja", "Australia", "Kanada", "Czechy", "Rosja", "Japonia", "Chiny"));
    public static List<String> FunduszeNazwa1 = Arrays.asList("Aggressive", "Financial", "Active", "Multi", "Aviva", "Allianz", "JP Morgan", "Wall Street");
    public static List<String> FunduszeNazwa2 = Arrays.asList("Growth", "Financial", "Investments", "Global", "+", "Investors", "Succes");
    public static List<String> Fundusze = new LinkedList<>();
    public static List<String> Miasta = Arrays.asList("Warszawa", "Londyn", "Paryż", "Nowy Jork", "Pekin", "Tokio", "Szanghai", "Frankfurt", "Berlin", "Bagno", "Swarzędz", "Czółenko");
    public static List<String> Adresy = Arrays.asList("Alternatywy", "Długa", "Krótka", "Średnia", "Główna", "Cicha", "Ciemna", "Mokra", "Wiejska", "Miejska", "Czarodziejska");
}
