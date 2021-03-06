package ro.jademy.domain.entities;

import ro.jademy.domain.entities.DVD.Builder;

public class DVD extends Media{
	private String directors;
	private String productionLabel;
	
	public DVD(String title, double price, String code, MediaGenre genre, String directors, String productionLabel){
		super(title, price, code, genre);
		this.directors = directors;
		this.productionLabel = productionLabel;		
	}

	public DVD() {
		// TODO Auto-generated constructor stub
	}

	public String getDirectors() {
		return directors;
	}


	public String getProductionLabel() {
		return productionLabel;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return directors;
	}
	 public static class Builder {
		 String title;
		 double price; 
		 String code;
		 MediaGenre genre;
		 String directors;
		 String productionLabel;
		 
		 public Builder title(String title) {
			 this. title= title;
			 return this;
		 }
		 public Builder price(double price) {
			 this. price= price;
			 return this;
		 }
		 public Builder code(String code) {
			 this. code= code;
			 return this;
		 }
		 public Builder genre(MediaGenre genre) {
			 this. genre= genre;
			 return this;
		 }
		 public Builder directors(String directors) {
			 this.directors= directors;
			 return this;
		 }
		 public Builder productionLabel(String productionLabel) {
			 this.productionLabel = productionLabel;
			 return this;
		 }
		
		 public DVD build() {
			 return new DVD(title, price, code, genre, directors, productionLabel);
		 }
	 }
}
