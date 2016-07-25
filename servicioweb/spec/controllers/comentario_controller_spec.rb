require 'rails_helper'

RSpec.describe ComentarioController, type: :controller do

	describe 'POST #Comentario a cliente' do
	    
	    it "Verificar que se haya comentado a un cliente" do  
	      user1 = FactoryGirl.create(:user) 
  		  user2 = FactoryGirl.create(:user1) 
  		  user3 = FactoryGirl.create(:user2) 
  		  user4 = FactoryGirl.create(:user3) 
  		  user5 = FactoryGirl.create(:user4) 
  		  user6 = FactoryGirl.create(:user5) 
	      params = { :comentario => ":V", :id_trabajador_que_comenta => 2, :id_usuario_que_recibe_comentario => 1 }
	      request.accept = "application/json"
	      post "dar_comentario_cliente", params 
	      resultado = JSON.parse(response.body)

	      resultado[0]['status'].should == "OK"
	    end


	    it "Obtener todos los comentarios de un cliente" do 
	      user1 = FactoryGirl.create(:user) 
  		  user2 = FactoryGirl.create(:user1) 
  		  user3 = FactoryGirl.create(:user2) 
  		  user4 = FactoryGirl.create(:user3) 
  		  user5 = FactoryGirl.create(:user4) 
  		  user6 = FactoryGirl.create(:user5) 
	      params = { :comentario => ":V", :id_trabajador_que_comenta => 2, :id_usuario_que_recibe_comentario => 1 }
	      request.accept = "application/json"
	      post "dar_comentario_cliente", params 
	      post "dar_comentario_cliente", params
	      post "dar_comentario_cliente", params

	      params1 = { :id_usuario_que_recibe_comentario => 1 }
	      request.accept = "application/json"
	      post "obtener_comentarios_cliente", params1 
	      resultado = JSON.parse(response.body)

	      resultado.length.should == 3

	    end

  	end


  	describe 'POST #Comentario a trabajador' do
	    
	    it "Verificar que se haya comentado a un trabajador" do  
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
  		 	perfil1 = FactoryGirl.create(:perfiltrabajador) 
  		 	perfil2 = FactoryGirl.create(:perfiltrabajador1) 
  		 	perfil3 = FactoryGirl.create(:perfiltrabajador2) 
  		 	perfil4 = FactoryGirl.create(:perfiltrabajador3) 
  		 	perfil5 = FactoryGirl.create(:perfiltrabajador4) 
  		 	perfil6 = FactoryGirl.create(:perfiltrabajador5) 
  		 	perfil7 = FactoryGirl.create(:perfiltrabajador6) 
	      params = { :comentario => ":V", :id_usuario_que_comenta => 2, :id_trabajador_que_recibe_comentario => 1, :id_perfil_trabajo => 1 }
	      request.accept = "application/json"
	      post "dar_comentario_trabajador", params 
	      resultado = JSON.parse(response.body)

	      resultado[0]['status'].should == "OK"
	    end


	    it "Obtener todos los comentarios de un trabajador" do 
	      user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
  		 	perfil1 = FactoryGirl.create(:perfiltrabajador) 
  		 	perfil2 = FactoryGirl.create(:perfiltrabajador1) 
  		 	perfil3 = FactoryGirl.create(:perfiltrabajador2) 
  		 	perfil4 = FactoryGirl.create(:perfiltrabajador3) 
  		 	perfil5 = FactoryGirl.create(:perfiltrabajador4) 
  		 	perfil6 = FactoryGirl.create(:perfiltrabajador5) 
  		 	perfil7 = FactoryGirl.create(:perfiltrabajador6) 
	      params = { :comentario => ":V", :id_usuario_que_comenta => 2, :id_trabajador_que_recibe_comentario => 1, :id_perfil_trabajo => 1 }
	      request.accept = "application/json"
	      post "dar_comentario_trabajador", params 
	      post "dar_comentario_trabajador", params
	      post "dar_comentario_trabajador", params

	      params1 = { :id_trabajador_que_recibe_comentario => 1, :id_perfil_trabajo => 1 }
	      request.accept = "application/json"
	      post "obtener_comentarios_trabajador", params1 
	      resultado = JSON.parse(response.body)

	      resultado.length.should == 3

	    end

  	end

end
