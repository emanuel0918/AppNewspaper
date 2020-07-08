package com.appnewspaper.model;


import android.os.Build;

import android.support.annotation.RequiresApi;

import com.appnewspaper.utils.Logger;
import com.appnewspaper.utils.SerializationUtils;

import org.json.simple.JSONObject;

import java.util.Hashtable;


public class Image extends ModelEntity {
	private int order;
	private String description;
	private int idArticle;
	private String image;
	
	/**
	 * Consructor of an Image, always through article, because an image shouldn't exist alone without one article
	 * @param order of the image within the article
	 * @param description
	 * @param idArticle - id of article of the image
	 * @param b64Image - data of the image
	 */
	public Image(int order, String description, int idArticle, String b64Image){
		this.id=-1;
		this.order = order;
		this.description = description;
		this.idArticle=idArticle;
		this.image = SerializationUtils.createScaledStrImage(b64Image,500,500);
	}
	
	/**
	 *
	 * @param jsonImage -- @SuppressWarnings("unchecked")
	 */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings("unchecked")
	protected Image(JSONObject jsonImage){
		try{
			id = Integer.parseInt(jsonImage.get("id").toString());
			order = Integer.parseInt(jsonImage.get("order").toString());
			description = jsonImage.getOrDefault("description","").toString().replaceAll("\\\\","");
			idArticle = Integer.parseInt(jsonImage.get("id_article").toString().replaceAll("\\\\",""));
			image = (jsonImage.get("data").toString().replaceAll("\\\\",""));
		}catch(Exception e){
			Logger.log(Logger.ERROR, "ERROR: Error parsing Image: from json"+jsonImage+"\n"+e.getMessage());
			throw new IllegalArgumentException("ERROR: Error parsing Image: from json"+jsonImage);
		}
	}	
	
	/**
	 * 
	 * @return
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * 
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 
	 * @return
	 */
	public int getIdArticle() {
		return idArticle;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Image [id=" + getId() + ", order=" + order + 
				", description=" + description + 
				", id_article=" + idArticle + 
				", data=" + image + "]";
	}
	
	protected Hashtable<String,String> getAttributes(){
		Hashtable<String,String> res = new Hashtable<String,String>();
		res.put("id_article", ""+idArticle);
		res.put("order", ""+order);
		res.put("description", description);
		res.put("data", image);
		res.put("media_type", "image/png");
		
		return res;
	}
	
	public String getImage(){
		return image;
	}
	

}
