package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.parse;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.food.type.FoodType;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.nutrient.NutrientContainer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JSONParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParseToFoodNullStringInput() {
        JSONParser.parseToFood(null, FoodType.SEARCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseToFoodNullFoodTypeInput() {
        JSONParser.parseToFood("", null);
    }

    @Test
    public void testParseToFoodReport() {
        String jsonString = "{\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\"" +
                ",\"dataType\":\"Branded\",\"gtinUpc\":\"009800146130\"," +
                "\"ingredients\":\"VEGETABLE\",\"labelNutrients\":{\"fat\":{\"value\":15.0}" +
                ",\"carbohydrates\":{\"value\":12.0}" + ",\"fiber\":{\"value\":0.99}" +
                ",\"protein\":{\"value\":2.0}," + "\"calories\":{\"value\":189.9}}}";

        Nutrient fat = new Nutrient(0, null, null, 15.0);
        Nutrient carbohydrates = new Nutrient(0, null, null, 12.0);
        Nutrient fiber = new Nutrient(0, null, null, 0.99);
        Nutrient protein = new Nutrient(0, null, null, 2.0);
        Nutrient calories = new Nutrient(0, null, null, 189.9);

        NutrientContainer labelNutrients = new NutrientContainer(fat, carbohydrates, fiber
                , protein, calories, null);

        Food expected = new FoodReport(415269, "RAFFAELLO, ALMOND COCONUT TREAT"
                , "Branded", "009800146130", null, "VEGETABLE"
                , null, labelNutrients);

        assertEquals(expected, JSONParser.parseToFood(jsonString, FoodType.REPORT));
    }

    @Test
    public void testParseToFoodSearch() {
        String separator = System.lineSeparator();
        String jsonString =
                "{\"totalHits\":1,\"currentPage\":1,\"totalPages\":1,\"pageList\":[1],\"foodSearchCriteria\":{\"query\":\"raffaello treat\",\"generalSearchInput\":\"raffaello treat\",\"pageNumber\":1,\"numberOfResultsPerPage\":50,\"pageSize\":50,\"requireAllWords\":true},\"foods\":[{\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"lowercaseDescription\":\"raffaello, almond coconut treat\",\"dataType\":\"Branded\",\"gtinUpc\":\"009800146130\",\"publishedDate\":\"2019-04-01\",\"brandOwner\":\"Ferrero U.S.A., Incorporated\",\"ingredients\":\"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.\",\"allHighlightFields\":\"\",\"score\":468.82208,\"foodNutrients\":[{\"nutrientId\":1087,\"nutrientName\":\"Calcium, Ca\",\"nutrientNumber\":\"301\",\"unitName\":\"MG\",\"derivationCode\":\"LCCD\",\"derivationDescription\":\"Calculated from a daily value percentage per serving size measure\",\"value\":133},{\"nutrientId\":1089,\"nutrientName\":\"Iron, Fe\",\"nutrientNumber\":\"303\",\"unitName\":\"MG\",\"derivationCode\":\"LCCD\",\"derivationDescription\":\"Calculated from a daily value percentage per serving size measure\",\"value\":1.2},{\"nutrientId\":1104,\"nutrientName\":\"Vitamin A, IU\",\"nutrientNumber\":\"318\",\"unitName\":\"IU\",\"derivationCode\":\"LCCD\",\"derivationDescription\":\"Calculated from a daily value percentage per serving size measure\",\"value\":0.0},{\"nutrientId\":1162,\"nutrientName\":\"Vitamin C, total ascorbic acid\",\"nutrientNumber\":\"401\",\"unitName\":\"MG\",\"derivationCode\":\"LCCD\",\"derivationDescription\":\"Calculated from a daily value percentage per serving size measure\",\"value\":0.0},{\"nutrientId\":1003,\"nutrientName\":\"Protein\",\"nutrientNumber\":\"203\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":6.67},{\"nutrientId\":1004,\"nutrientName\":\"Total lipid (fat)\",\"nutrientNumber\":\"204\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":50.0},{\"nutrientId\":1005,\"nutrientName\":\"Carbohydrate, by difference\",\"nutrientNumber\":\"205\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":40.0},{\"nutrientId\":1008,\"nutrientName\":\"Energy\",\"nutrientNumber\":\"208\",\"unitName\":\"KCAL\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":633},{\"nutrientId\":2000,\"nutrientName\":\"Sugars, total including NLEA\",\"nutrientNumber\":\"269\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":33.3},{\"nutrientId\":1079,\"nutrientName\":\"Fiber, total dietary\",\"nutrientNumber\":\"291\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":3.3},{\"nutrientId\":1093,\"nutrientName\":\"Sodium, Na\",\"nutrientNumber\":\"307\",\"unitName\":\"MG\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":117},{\"nutrientId\":1257,\"nutrientName\":\"Fatty acids, total trans\",\"nutrientNumber\":\"605\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":0.0},{\"nutrientId\":1258,\"nutrientName\":\"Fatty acids, total saturated\",\"nutrientNumber\":\"606\",\"unitName\":\"G\",\"derivationCode\":\"LCCS\",\"derivationDescription\":\"Calculated from value per serving size measure\",\"value\":30.0},{\"nutrientId\":1253,\"nutrientName\":\"Cholesterol\",\"nutrientNumber\":\"601\",\"unitName\":\"MG\",\"derivationCode\":\"LCSL\",\"derivationDescription\":\"Calculated from a less than value per serving size measure\",\"value\":17.0}]}],\"aggregations\":{\"dataType\":{\"Branded\":1},\"nutrients\":{}}}";
        Food searchFood = JSONParser.parseToFood(jsonString, FoodType.SEARCH);
        String expected = "SEARCH RESULT: raffaello treat" + separator +
                "Total: 1" + separator +
                "FOOD: " + separator +
                "{" + separator +
                "  fdcId: 415269" + separator +
                "  description: RAFFAELLO, ALMOND COCONUT TREAT" + separator +
                "  dataType: Branded" + separator +
                "  gtinUpc: 009800146130" + separator +
                "  ingredients: VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT." +
                separator +
                "  NUTRIENTS: " + separator +
                "  {" + separator +
                "    nutrientId: 1087" + separator +
                "    nutrientName: Calcium, Ca" + separator +
                "    value: 133.0" + separator +
                "    unitName: MG" + separator +
                "" + separator +
                "    nutrientId: 1089" + separator +
                "    nutrientName: Iron, Fe" + separator +
                "    value: 1.2" + separator +
                "    unitName: MG" + separator +
                "" + separator +
                "    nutrientId: 1104" + separator +
                "    nutrientName: Vitamin A, IU" + separator +
                "    value: 0.0" + separator +
                "    unitName: IU" + separator +
                "" + separator +
                "    nutrientId: 1162" + separator +
                "    nutrientName: Vitamin C, total ascorbic acid" + separator +
                "    value: 0.0" + separator +
                "    unitName: MG" + separator +
                "" + separator +
                "    nutrientId: 1003" + separator +
                "    nutrientName: Protein" + separator +
                "    value: 6.67" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1004" + separator +
                "    nutrientName: Total lipid (fat)" + separator +
                "    value: 50.0" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1005" + separator +
                "    nutrientName: Carbohydrate, by difference" + separator +
                "    value: 40.0" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1008" + separator +
                "    nutrientName: Energy" + separator +
                "    value: 633.0" + separator +
                "    unitName: KCAL" + separator +
                "" + separator +
                "    nutrientId: 2000" + separator +
                "    nutrientName: Sugars, total including NLEA" + separator +
                "    value: 33.3" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1079" + separator +
                "    nutrientName: Fiber, total dietary" + separator +
                "    value: 3.3" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1093" + separator +
                "    nutrientName: Sodium, Na" + separator +
                "    value: 117.0" + separator +
                "    unitName: MG" + separator +
                "" + separator +
                "    nutrientId: 1257" + separator +
                "    nutrientName: Fatty acids, total trans" + separator +
                "    value: 0.0" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1258" + separator +
                "    nutrientName: Fatty acids, total saturated" + separator +
                "    value: 30.0" + separator +
                "    unitName: G" + separator +
                "" + separator +
                "    nutrientId: 1253" + separator +
                "    nutrientName: Cholesterol" + separator +
                "    value: 17.0" + separator +
                "    unitName: MG" + separator +
                "  }" + separator +
                "}" + separator;
        String actual = searchFood.toString(true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseToFoodReportRaffaello() {
        String separator = System.lineSeparator();

        String jsonString =
                "{\"foodComponents\":[],\"foodAttributes\":[],\"foodPortions\":[],\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"publicationDate\":\"4/1/2019\",\"foodNutrients\":[{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1253,\"number\":\"601\",\"name\":\"Cholesterol\",\"rank\":15700,\"unitName\":\"mg\"},\"foodNutrientDerivation\":{\"id\":73,\"code\":\"LCSL\",\"description\":\"Calculated from a less than value per serving size measure\",\"foodNutrientSource\":{\"id\":9,\"code\":\"12\",\"description\":\"Manufacturer's analytical; partial documentation\"}},\"id\":6391876,\"amount\":17.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1005,\"number\":\"205\",\"name\":\"Carbohydrate, by difference\",\"rank\":1110,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348028,\"amount\":40.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1257,\"number\":\"605\",\"name\":\"Fatty acids, total trans\",\"rank\":15400,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348033,\"amount\":0E-8},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1008,\"number\":\"208\",\"name\":\"Energy\",\"rank\":300,\"unitName\":\"kcal\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348029,\"amount\":633.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1079,\"number\":\"291\",\"name\":\"Fiber, total dietary\",\"rank\":1200,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348031,\"amount\":3.30000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1258,\"number\":\"606\",\"name\":\"Fatty acids, total saturated\",\"rank\":9700,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348034,\"amount\":30.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1093,\"number\":\"307\",\"name\":\"Sodium, Na\",\"rank\":5800,\"unitName\":\"mg\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348032,\"amount\":117.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":2000,\"number\":\"269\",\"name\":\"Sugars, total including NLEA\",\"rank\":1510,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348030,\"amount\":33.33000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1004,\"number\":\"204\",\"name\":\"Total lipid (fat)\",\"rank\":800,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\"},\"id\":5348027,\"amount\":50.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1003,\"number\":\"203\",\"name\":\"Protein\",\"rank\":600,\"unitName\":\"g\"},\"foodNutrientDerivation\":{\"id\":70,\"code\":\"LCCS\",\"description\":\"Calculated from value per serving size measure\",\"foodNutrientSource\":{\"id\":9,\"code\":\"12\",\"description\":\"Manufacturer's analytical; partial documentation\"}},\"id\":5348026,\"amount\":6.67000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1087,\"number\":\"301\",\"name\":\"Calcium, Ca\",\"rank\":5300,\"unitName\":\"mg\"},\"foodNutrientDerivation\":{\"id\":75,\"code\":\"LCCD\",\"description\":\"Calculated from a daily value percentage per serving size measure\"},\"id\":3649548,\"amount\":133.00000000},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1104,\"number\":\"318\",\"name\":\"Vitamin A, IU\",\"rank\":7500,\"unitName\":\"IU\"},\"foodNutrientDerivation\":{\"id\":75,\"code\":\"LCCD\",\"description\":\"Calculated from a daily value percentage per serving size measure\"},\"id\":3649550,\"amount\":0E-8},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1162,\"number\":\"401\",\"name\":\"Vitamin C, total ascorbic acid\",\"rank\":6300,\"unitName\":\"mg\"},\"foodNutrientDerivation\":{\"id\":75,\"code\":\"LCCD\",\"description\":\"Calculated from a daily value percentage per serving size measure\"},\"id\":3649551,\"amount\":0E-8},{\"type\":\"FoodNutrient\",\"nutrient\":{\"id\":1089,\"number\":\"303\",\"name\":\"Iron, Fe\",\"rank\":5400,\"unitName\":\"mg\"},\"foodNutrientDerivation\":{\"id\":75,\"code\":\"LCCD\",\"description\":\"Calculated from a daily value percentage per serving size measure\",\"foodNutrientSource\":{\"id\":9,\"code\":\"12\",\"description\":\"Manufacturer's analytical; partial documentation\"}},\"id\":3649549,\"amount\":1.20000000}],\"dataType\":\"Branded\",\"foodClass\":\"Branded\",\"modifiedDate\":\"7/14/2017\",\"availableDate\":\"7/14/2017\",\"brandOwner\":\"Ferrero U.S.A., Incorporated\",\"dataSource\":\"LI\",\"brandedFoodCategory\":\"Chocolate\",\"gtinUpc\":\"009800146130\",\"householdServingFullText\":\"3 PIECES\",\"ingredients\":\"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.\",\"marketCountry\":\"United States\",\"servingSize\":30.00000000,\"servingSizeUnit\":\"g\",\"foodUpdateLog\":[{\"foodAttributes\":[],\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"publicationDate\":\"4/1/2019\",\"dataType\":\"Branded\",\"foodClass\":\"Branded\",\"modifiedDate\":\"7/14/2017\",\"availableDate\":\"7/14/2017\",\"brandOwner\":\"Ferrero U.S.A., Incorporated\",\"dataSource\":\"LI\",\"brandedFoodCategory\":\"Chocolate\",\"gtinUpc\":\"009800146130\",\"householdServingFullText\":\"3 PIECES\",\"ingredients\":\"VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.\",\"marketCountry\":\"United States\",\"servingSize\":30.00000000,\"servingSizeUnit\":\"g\"}],\"labelNutrients\":{\"fat\":{\"value\":15.0000000000000000},\"saturatedFat\":{\"value\":9.0000000000000000},\"transFat\":{\"value\":0E-16},\"cholesterol\":{\"value\":5.1000000000000000},\"sodium\":{\"value\":35.1000000000000000},\"carbohydrates\":{\"value\":12.0000000000000000},\"fiber\":{\"value\":0.9900000000000000},\"sugars\":{\"value\":9.9990000000000000},\"protein\":{\"value\":2.0010000000000000},\"calcium\":{\"value\":39.9000000000000000},\"iron\":{\"value\":0.3600000000000000},\"calories\":{\"value\":189.9000000000000000}}}";
        Food foodReport = JSONParser.parseToFood(jsonString, FoodType.REPORT);
        String expected = "REPORT RESULT: 415269" + separator +
                "FOOD: " + separator +
                "{" + separator +
                "  fdcId: 415269" + separator +
                "  description: RAFFAELLO, ALMOND COCONUT TREAT" + separator +
                "  dataType: Branded" + separator +
                "  gtinUpc: 009800146130" + separator +
                "  ingredients: VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT." + separator +
                "  CONTENT: " + separator +
                "  {" + separator +
                "    fat: 15.0" + separator +
                "    carbohydrates: 12.0" + separator +
                "    protein: 2.001" + separator +
                "    fiber: 0.99" + separator +
                "    calories: 189.9" + separator +
                "  }" + separator +
                "  NUTRIENTS: " + separator +
                "  {" + separator +
                "    nutrientId: 1253" + separator +
                "    nutrientName: Cholesterol" + separator +
                "    value: 0.0" + separator +
                "    unitName: mg" + separator +
                "" + separator +
                "    nutrientId: 1104" + separator +
                "    nutrientName: Vitamin A, IU" + separator +
                "    value: 0.0" + separator +
                "    unitName: IU" + separator +
                "" + separator +
                "    nutrientId: 1079" + separator +
                "    nutrientName: Fiber, total dietary" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 1005" + separator +
                "    nutrientName: Carbohydrate, by difference" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 1162" + separator +
                "    nutrientName: Vitamin C, total ascorbic acid" + separator +
                "    value: 0.0" + separator +
                "    unitName: mg" + separator +
                "" + separator +
                "    nutrientId: 1008" + separator +
                "    nutrientName: Energy" + separator +
                "    value: 0.0" + separator +
                "    unitName: kcal" + separator +
                "" + separator +
                "    nutrientId: 1093" + separator +
                "    nutrientName: Sodium, Na" + separator +
                "    value: 0.0" + separator +
                "    unitName: mg" + separator +
                "" + separator +
                "    nutrientId: 1257" + separator +
                "    nutrientName: Fatty acids, total trans" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 1087" + separator +
                "    nutrientName: Calcium, Ca" + separator +
                "    value: 0.0" + separator +
                "    unitName: mg" + separator +
                "" + separator +
                "    nutrientId: 1089" + separator +
                "    nutrientName: Iron, Fe" + separator +
                "    value: 0.0" + separator +
                "    unitName: mg" + separator +
                "" + separator +
                "    nutrientId: 1258" + separator +
                "    nutrientName: Fatty acids, total saturated" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 1004" + separator +
                "    nutrientName: Total lipid (fat)" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 1003" + separator +
                "    nutrientName: Protein" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "" + separator +
                "    nutrientId: 2000" + separator +
                "    nutrientName: Sugars, total including NLEA" + separator +
                "    value: 0.0" + separator +
                "    unitName: g" + separator +
                "  }" + separator +
                "}" + separator;
        String actual = foodReport.toString(true);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseToFoodWhenFoodTypeIsOther() {
        assertNull(JSONParser.parseToFood("", null));
    }
}
