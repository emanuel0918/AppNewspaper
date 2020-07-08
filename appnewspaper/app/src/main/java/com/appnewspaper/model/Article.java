package com.appnewspaper.model;


import com.appnewspaper.utils.Logger;
import com.appnewspaper.utils.SerializationUtils;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import org.json.simple.JSONObject;

import java.util.Date;
import java.util.Hashtable;


public class Article extends ModelEntity {
	
	private String titleText;
	private String category;
	private String abstractText;
	private String bodyText;
	private String subtitle;
	private int idUser;
	private Image mainImage;
	private String imageDescription;
	private String thumbnail;
	private Date lastUpdate;



	private String parseStringFromJson(JSONObject jsonArticle, String key, String def){
		Object in = jsonArticle.getOrDefault(key,def);
		return (in==null?def:in).toString();
	}

	@SuppressWarnings("unchecked")
	public Article(JSONObject jsonArticle){
		try{
			id = Integer.parseInt(jsonArticle.get("id").toString());
			idUser = Integer.parseInt(parseStringFromJson(jsonArticle,"id_user","0"));
			titleText = parseStringFromJson(jsonArticle,"title","").replaceAll("\\\\","");
			category = parseStringFromJson(jsonArticle,"category","").replaceAll("\\\\","");
			abstractText = parseStringFromJson(jsonArticle,"abstract","").replaceAll("\\\\","");
			bodyText = parseStringFromJson(jsonArticle,"body","").replaceAll("\\\\","");
			subtitle = parseStringFromJson(jsonArticle,"subtitle","").replaceAll("\\\\","");
			
			imageDescription = parseStringFromJson(jsonArticle,"image_description","").replaceAll("\\\\","");
			thumbnail = parseStringFromJson(jsonArticle,"thumbnail_image","").replaceAll("\\\\","");

			lastUpdate = SerializationUtils.dateFromString(parseStringFromJson(jsonArticle,"update_date","").replaceAll("\\\\",""));

			String imageData = parseStringFromJson(jsonArticle,"image_data","").replaceAll("\\\\","");
		
			if (imageData!=null && !imageData.isEmpty())
				mainImage = new Image(1, imageDescription, id, imageData);
		}catch(Exception e){
			Logger.log(Logger.ERROR, "ERROR: Error parsing Article: from json"+jsonArticle+"\n"+e.getMessage());
			throw new IllegalArgumentException("ERROR: Error parsing Article: from json"+jsonArticle);
		}
	}
	
	public Article(String category, String titleText, String abstractText, String body, String subtitle, String idUser){
		id = -1;
		this.category = category;
		this.idUser = Integer.parseInt(idUser);
		this.abstractText = abstractText;
		this.titleText = titleText;
		bodyText = body;
		this.subtitle = subtitle;
	}
	
	public void setId(int id){
		if (id <1){
			throw new IllegalArgumentException("ERROR: Error setting a wrong id to an article:"+id);
		}
		if (this.id>0 ){
			throw new IllegalArgumentException("ERROR: Error setting an id to an article with an already valid id:"+this.id);
		}
		this.id = id;
	}
	
	public String getTitleText() {
		return titleText;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category= category;
	}
	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}
	public String getAbstractText() {
		return abstractText;
	}
	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}
	public String getBodyText() {
		return bodyText;
	}
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	public String getSubtitleText() {
		return subtitle;
	}
	public void setSubtitleText(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public int getIdUser(){
		return idUser;
	}
	public Image getImage() throws ServerCommunicationError {
		Image image = mainImage;
		if (mainImage==null && thumbnail!=null && !thumbnail.isEmpty()){
			image = new Image(1,"",getId(),thumbnail);
		}
		return image;
	}

	public void setImage(Image image) {
		this.mainImage = image;
	}
	
	public Image addImage(String b64Image, String description) throws ServerCommunicationError{	
		int order = 1;
		Image img =new Image(order, description, getId(), b64Image);
		mainImage= img;
		return img;
	}
	
	@Override
	public String toString() {
		return "Article [id=" + getId()
				+", titleText=" + titleText  
				+", abstractText=" + abstractText 
				+  ", bodyText="	+ bodyText + ", subtitleText=" + subtitle
				//+ ", publicationDate=" + Utils.dateToString(publicationDate) 
				+", image_description=" + imageDescription
				+", image_data=" + mainImage
				+", thumbnail=" + thumbnail 
				+ "]";
	}
	
	public Hashtable<String,String> getAttributes(){
		Hashtable<String,String> res = new Hashtable<String,String>();
		res.put("category", category);
		res.put("abstract", abstractText);
		res.put("title", titleText);
		res.put("body", bodyText);
		res.put("subtitle", subtitle);
		if (mainImage!=null){
			res.put("image_data", mainImage.getImage());
			res.put("image_media_type", "image/png");
		}
		
		if (mainImage!=null && mainImage.getDescription()!=null && !mainImage.getDescription().isEmpty())
			res.put("image_description", mainImage.getDescription());
		else if (imageDescription!=null && !imageDescription.isEmpty())
			res.put("image_description", imageDescription);

		//res.put("publication_date", publicationDate==null?null:Utils.dateToString(publicationDate));
		return res;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
}
