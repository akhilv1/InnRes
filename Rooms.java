class Rooms{
	String room;
	int rate;
	float popularity;
	String latest;
	int length;
	int maxOcc;
	String bed;
	int numBeds;
	InnCalandar cal;
	
	public Rooms(){
		this.room = "";
		this.rate = 0;
		this.popularity = 0;
		this.latest = "";
		this.length = 0;
		this.maxOcc = 0;
		this.bed = "";
		this.numBeds = 0;
		InnCalandar cal = new InnCalandar();
	}
	
	// TODO add date finding functions for reservations
}
