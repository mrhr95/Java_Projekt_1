package view.routenbeschreibung;

import model.dijkstraData.Kante;
import model.dijkstraData.Knoten;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Routenbeschreibung {
	private double kmZm = 3.0;		//Distanz in Kilometern, ab der die Entfernung in Kilometern statt in Metern angegeben wird
	private String [] Routenbeschreibung;
	private String [] Abbiegegrade = {"weiter geradeaus fahren","halb","nach ","scharf nach "};
	private String [] Richtungen = {"rechts","links"};
	private String [] Auf = {" auf die ", " und der ", " weiter folgen"};
	private LinkedList<Kante> listeDerKantenAufDerRoute;

	/**
	 * @param routenbeschreibung
	 */
	public Routenbeschreibung(String[] routenbeschreibung) {
		super();
		Routenbeschreibung = routenbeschreibung;
	}
	
	 public String[] erzeugeRoutenbeschreibung(LinkedList<Kante> listeDerKantenAufDerRoute) {
		 int s = listeDerKantenAufDerRoute.size();
		 int abbiegegrad;
		 String was = null;
		 String wohin = null;
		 String wieWeit = null;
		 double Teilentfernung = 0;
		 double gesamtEntfernung = 0;
		 String [] Richtung_vorl = {"",""};
		 String [] Routenbeschreibung = new String[s+3];
		 
		 for (int i=0; i<(s-1); i++) {
			 Kante k = listeDerKantenAufDerRoute.get(i);
			 Kante n = listeDerKantenAufDerRoute.get(i+1);
			 Teilentfernung = k.getGewicht("dist");
			 gesamtEntfernung += Teilentfernung;
			 wieWeit = this.getStringWieWeit(Teilentfernung); // Entfernung in m oder km
			 wohin = this.getStringWohin(k.getName(), n.getName()); // abbiegender Stra�enverlauf oder abbiegen auf andere Stra�e
			 Richtung_vorl[1] = Richtungen[k.nextIsToTheLeft(n)?1:0].concat(" abbiegen");
			 abbiegegrad = (int)Math.ceil((k.getWinkelGradNaechst(n)+22.5)/45);
			 was = Abbiegegrade[abbiegegrad].concat(Richtung_vorl[abbiegegrad!=0 ? 1 : 0]);
			 Routenbeschreibung[i+2] = String.format("Bitte in " + wieWeit + was + wohin + ".");
		 }
		 gesamtEntfernung += listeDerKantenAufDerRoute.getLast().getGewicht("dist");
		 wieWeit = this.getStringWieWeit(listeDerKantenAufDerRoute.getLast().getGewicht("dist")); // Entfernung in m oder km
		 Routenbeschreibung[0] = String.format("Die L�nge der Berechneten Route betr�gt: " + this.getStringWieWeit(gesamtEntfernung) + ".");
		 //Routenbeschreibung[1] = String.format("Bitte in Richtung " + this.getHimmelsrichtung(listeDerKantenAufDerRoute.getFirst().getGeometry(), listeDerKantenAufDerRoute.get(1).getGeometry()) + " losfahren.");
		 Routenbeschreibung[1] = "Bitte losfahren!";
		 Routenbeschreibung[s+2] = String.format("In " + wieWeit + " haben sie ihr Ziel erreicht."); //Lage des Ziels kann ich noch nachreichen, ist nciht viel aufwand, vorrausgestezt ich hab die Zielkoordinaten.
		 return Routenbeschreibung;
	 }
	 
	 private String getHimmelsrichtung (Point2D.Double p1,Point2D.Double p2) {
		 String [] Himmelsrichtungen = {"Norden","Nord-Osten","Osten","S�d-Osten","S�den","S�d-Westen","Westen","Nord-Westen"};
		 double richtungGrad = Math.atan2(p2.getX()-p1.getX(),p2.getY()-p1.getY())*360/Math.PI;
		 return Himmelsrichtungen[(int)Math.ceil(((richtungGrad + 360/16)%400)/360*8)];
	 }
	 
	 private String getStringWohin (String aktuellerName, String nachsterName) {
		 if (nachsterName == null) {
			 return null;
		 } else if (nachsterName == aktuellerName) {
			 return String.format(Auf[2] + nachsterName + Auf[3]);
		 } else {
			 return String.format(Auf[1] + nachsterName);
		 }
	 }
	 
	 private String getStringWieWeit (double distanz) {
		 if (distanz < kmZm) {
			 return String.format(" %1$f m", distanz*1000);
		 } else {
			 return String.format(" %1$.1f km", distanz);
		 }
	 }

}
