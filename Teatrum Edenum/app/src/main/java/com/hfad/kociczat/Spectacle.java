package com.hfad.kociczat;

public class Spectacle {
    private String name;
    private int imageResourceId;

    public Spectacle(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }
    public static final Spectacle [] performances = {
            new Spectacle("Najlepszy Przyjaciel", R.drawable.najlepszy_przyjaciel),
            new Spectacle("Najpiękniejszy Prezent", R.drawable.najpiekniejszy_prezent),
            new Spectacle("Ukryte Bogactwo", R.drawable.ukryte_bogactwo),
            new Spectacle("Opowieść o dobrym człowieku", R.drawable.opowiesc_o_dobrym_czlowieku),
            new Spectacle("Arka Noego", R.drawable.arka_noego),
            new Spectacle("Historia Daniela w jaskini Lwów", R.drawable.daniel_w_jaskini),
            new Spectacle("Jonasz i niezwykła Ryba", R.drawable.jonasz),
            new Spectacle("Legenda o św. Mikołaju Biskupie", R.drawable.legenda),
            new Spectacle("Odważny Pastuszek", R.drawable.odwazny),
            new Spectacle("Opowieść Wigilijna", R.drawable.opowiesc_wigilijna),
            new Spectacle("Prawdziwy Skarb", R.drawable.prawdziwy_skarb),
            new Spectacle("Przypowieść o Talentach", R.drawable.talenty),
            new Spectacle("Zagubiona Owieczka", R.drawable.owieczka),
            new Spectacle("Wizyta św. Mikołaja", R.drawable.mikolaj)
    };

    public String getName() {
        return name;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
}
