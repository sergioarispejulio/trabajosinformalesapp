require 'rails_helper'

RSpec.describe UsuarioController, type: :controller do

	describe 'POST #crear' do
	    it "Crear usuario correcto" do  
	    	tipotrabajo1 = FactoryGirl.create(:tipotrabajo) 
  		 	tipotrabajo2 = FactoryGirl.create(:tipotrabajo1) 
	      	params = {:nickname => "prueba", :telefono => 123456789, :password => "123", :password_confirmation => "123", :email => "prueba@gmail.com", :nombre => "Nombre",  :apellido => "Apellido", :ciudad => "Cochabamba", :ci => "123", :fecha_nacimiento => "2014/2/3", :modelo => "modelo1", :so => "Android"}
	    	request.accept = "application/json"
	    	post "crear", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['status'].should == "OK"
	      	Perfiltrabajador.all.length.should == 2
	    end

	    it "Crear usuario con nickname duplicado" do  
	      	params = {:nickname => "prueba", :telefono => 123456789, :password => "123", :password_confirmation => "123", :email => "prueba@gmail.com", :nombre => "Nombre",  :apellido => "Apellido", :ciudad => "Cochabamba", :ci => "123", :fecha_nacimiento => "2014/2/3", :modelo => "modelo1", :so => "Android"}

	    	request.accept = "application/json"
	    	post "crear", params 
	    	params = {:nickname => "prueba", :telefono => 987654321, :password => "123", :password_confirmation => "123", :email => "prueba@gmail.com", :nombre => "Nombre",  :apellido => "Apellido", :ciudad => "Cochabamba", :ci => "123", :fecha_nacimiento => "2014/2/3", :modelo => "modelo1", :so => "Android"}
	    	post "crear", params 
	      	resultado = JSON.parse(response.body)

	      	#post "crearusuario", prueba_params , request_headers
	      	resultado[0]['status'].should == "El nombre de usuario que ingreso ya esta en uso"
	    end

	    it "Crear usuario con telefono duplicado" do 

	      	params = {:nickname => "prueba", :telefono => 123456789, :password => "123", :password_confirmation => "123", :email => "prueba@gmail.com", :nombre => "Nombre",  :apellido => "Apellido", :ciudad => "Cochabamba", :ci => "123", :fecha_nacimiento => "2014/2/3", :modelo => "modelo1", :so => "Android"}

	    	request.accept = "application/json"
	    	post "crear", params 
	    	params = {:nickname => "prueba1", :telefono => 123456789, :password => "123", :password_confirmation => "123", :email => "prueba@gmail.com", :nombre => "Nombre",  :apellido => "Apellido", :ciudad => "Cochabamba", :ci => "123", :fecha_nacimiento => "2014/2/3", :modelo => "modelo1", :so => "Android"}
	    	post "crear", params 
	      	resultado = JSON.parse(response.body)

	      	#post "crearusuario", prueba_params , request_headers
	      	resultado[0]['status'].should == "El telefono que ingreso ya esta en uso"
	    end

  	end




  	describe 'POST #modificar informacion (menos nickname y contraseña)' do

  		it "Modificar informacion de un usuario con telefono nuevo" do
  			user = FactoryGirl.create(:user) 
  			params = {:id => user.id, :telefono => 666666669, :ciudad => "Cochabamba", :url_foto => "pureaba"}

	    	request.accept = "application/json"
	    	post "editar_informacion", params 
	      	resultado = JSON.parse(response.body)

	      	#post "crearusuario", prueba_params , request_headers
	      	resultado[0]['status'].should == "OK"
  		end 

  		it "Modificar informacion de un usuario con telefono repetido" do
  			user = FactoryGirl.create(:user) 
  			user1 = FactoryGirl.create(:user1) 
  			params = {:id => user.id, :telefono => 123456788, :ciudad => "Cochabamba", :url_foto => "pureaba"}

	    	request.accept = "application/json"
	    	post "editar_informacion", params
	    	resultado = JSON.parse(response.body)

	      	#post "crearusuario", prueba_params , request_headers
	      	resultado[0]['status'].should == "El telefono que ingreso ya esta en uso"
  		end 

  	end




  	describe 'POST #buscar trabajador' do
  		it "Obtener lista de trabajadores de acuerdo a un tipo de trabajo" do  
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
	      	params = {:tipo_trabajo => 1}
	    	request.accept = "application/json"
	    	post "obtener_lista_de_trabajadores_con_perfil_determinado", params 
	      	resultado = JSON.parse(response.body)

	      	resultado.length.should == 4
	    end
 
	    it "Obtener a un trabajador en un determinado tipo de trabajo con comentarios y tiene perfil" do
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
	    	perfil1 = FactoryGirl.create(:perfiltrabajador) 
	    	comentario1 = FactoryGirl.create(:comentarioatrabajador)
	    	comentario2 = FactoryGirl.create(:comentarioatrabajador)
	    	params = {:id_trabajador => 1, :id_tipo_trabajo => 1, :id_usuario => 2}
	    	request.accept = "application/json"
	    	post "obtener_informacion_trabajador_con_perfil_determinado", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['usuario']['nombre'].should == "Nombre"
	      	resultado[0]['status_perfil'].should == true
	      	resultado[0]['status_comentarios'].should == true
	      	resultado[0]['perfil']['id_tipo_trabajo'].should == 1
	      	resultado[0]['comentarios'].length.should == 2
	      	resultado[0]['nombres_comentarios'].length.should == 2
	      	resultado[0]['status_hizo_puntuacion'].should == false
		end

		it "Obtener a un trabajador en un determinado tipo de trabajo sin perfil ni comentarios" do
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
	    	params = {:id_trabajador => 1, :id_tipo_trabajo => 1, :id_usuario => 2}
	    	request.accept = "application/json"
	    	post "obtener_informacion_trabajador_con_perfil_determinado", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['status_perfil'].should == false
	      	resultado[0]['status_comentarios'].should == false
		end

		it "Obtener a un trabajador en un determinado tipo de trabajo con perfil, pero sin comentarios" do
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
  		 	perfil1 = FactoryGirl.create(:perfiltrabajador) 
	    	params = {:id_trabajador => 1, :id_tipo_trabajo => 1, :id_usuario => 2}
	    	request.accept = "application/json"
	    	post "obtener_informacion_trabajador_con_perfil_determinado", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['status_perfil'].should == true
	      	resultado[0]['status_comentarios'].should == false
		end

  	end




  	describe 'POST #buscar cliente' do
  		it "Obtener a un cliente determinado con comentarios" do
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
  		 	comentario = FactoryGirl.create(:comentarioacliente) 
	    	params = {:id_cliente => 1}
	    	request.accept = "application/json"
	    	post "obtener_informacion_cliente", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['usuario']['nombre'].should == "Nombre"
	      	resultado[0]['status_comentarios'].should == true
	      	resultado[0]['comentarios'].length.should == 1
	      	resultado[0]['nombres_comentarios'].length.should == 1
		end

		it "Obtener a un cliente determinado sin comentarios" do
	    	user1 = FactoryGirl.create(:user) 
  		 	user2 = FactoryGirl.create(:user1) 
  		 	user3 = FactoryGirl.create(:user2) 
  		 	user4 = FactoryGirl.create(:user3) 
  		 	user5 = FactoryGirl.create(:user4) 
  		 	user6 = FactoryGirl.create(:user5) 
	    	params = {:id_cliente => 1}
	    	request.accept = "application/json"
	    	post "obtener_informacion_cliente", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['usuario']['nombre'].should == "Nombre"
	      	resultado[0]['status_comentarios'].should == false
		end

  	end

end
