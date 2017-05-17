public class EntityScore
{
    private int temp_index;
    private String url_index;
    private String entity;
    private double score;

    // constructor
    public EntityScore(int temp, String url, String name, double score_val){
        this.temp_index = temp;
        this.url_index = url;
        this.entity = name;
        this.score = score_val;
    }

    public int getTempIndex() {
        return temp_index;
    }

    public String getUrl() {
        return url_index;
    }

    public String getEntity() {
        return entity;
    }

    public double getScore() {
        return score;
    }

    public void setTempIndex(int temp) {
        this.temp_index = temp;
    }

    public void setUrl(String url) {
        this.url_index = url;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setScore(double score) {
        this.score = score;
    }
}