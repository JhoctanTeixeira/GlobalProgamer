package br.com.fiap.bean;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;

@Named
@RequestScoped
public class UserBean {

	private User user = new User();
	
	private UserDao userDao = new UserDao();
	private UploadedFile image;
	
	public void save() throws IOException  {

		System.out.println(this.user);
		
		System.out.println(image.getFileName()); 
		
		ServletContext servletContext = (ServletContext) FacesContext
															.getCurrentInstance()
															.getExternalContext()
															.getContext();
		String servletPath = servletContext.getRealPath("/");
		
		System.err.println(servletPath);
		
		FileOutputStream out = 
				new FileOutputStream(servletPath + "\\images\\" + image.getFileName());
		out.write(image.getContent());
		out.close();
		
		user.setImagePath("\\images\\" + image.getFileName());
		
		userDao.create(user);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Usuário cadastrado com sucesso"));
		
	}
	
	public List<User> listAll() {
		User user = 
		(User) FacesContext
		.getCurrentInstance()
		.getExternalContext()
		.getSessionMap()
		.get("user");
		return userDao.listAll(user);
	}
	
	public String login(){
		User userx = userDao.exist(user);
		if (userx != null) {
			//salvar o usuario logado na secao
			FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSessionMap()
				.put("user", userx);
			
			return "setups";
		}
		
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getFlash()
			.setKeepMessages(true);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Login inválido"));

		return "login?faces-redirect=true";
	}
	
	public String logout() {
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getSessionMap()
			.remove("user");
		
		return "login";
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}



}
