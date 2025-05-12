package com.TicTacToe.Campaign;

public class Campaign {
    public static int ROWS = 18;
    private static String winner;
    private CampaignStage1 campaignStage1;
    CampaignStage2 campaignStage2;
    public static int stage;
    public Campaign(String name){
        stage =1;
        winner = name;
    }
    public void PlayCamp1(){
        CampaignStage1.newRow =ROWS;
        campaignStage1 = new CampaignStage1(winner);
    }
}
