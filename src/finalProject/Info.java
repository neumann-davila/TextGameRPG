/*
 * Author: Neumann Davila
 * Date:   Jan 13, 2023
 * Description:
 *
 *
 */
package finalProject;

public class Info {
    private String[] infoText;

    public void display() {
        for(String currentText : infoText) {
            System.out.println("\033[0;94m" + currentText);
            try{
                Thread.sleep(2000);
            }
            catch(Exception e) {

            }
        }
    }

    public Info(String infoText) {
        this.infoText = infoText.split("`");
    }
}
