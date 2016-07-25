require 'rails_helper'



RSpec.describe SessionController, type: :controller do

	describe 'POST #crear' do
	    it "Crear seccion con un usuario existente en el sistema" do  
	      usuario = FactoryGirl.create(:user)
	      #puts User.first.id
	      #puts User.first.email
	      #puts User.first.salt
	      params = { :nickname => "prueba", :password => "123", :modelo => "modelo1", :so => "Android" }
	      #request_headers = { "accept" => "application/json", "Content-Type" => "application/json" }

	      request.accept = "application/json"

	      #post "crear", params , request_headers

	      post "crear", params 
	      resultado = JSON.parse(response.body)

	      #puts resultado.to_s + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
	      #puts resultado[0]['token']
	      resultado[0]['status'].should == "OK"
	      resultado[0]['token'].should_not == "" 
	      resultado[0]['id_usuario'].should == usuario.id
	    end

	    it "Crear seccion con un usuario no existente en el sistema" do 
	    	usuario = FactoryGirl.create(:user)
		    params = { :token => "acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%"}
	        request.accept = "application/json"

		    post "crear", params 
		    resultado = JSON.parse(response.body)

		    resultado[0]['status'].should == "Nombre de usuario o contraseña incorrectos"
		    resultado[0]['token'].should == ""
	        resultado[0]['id_usuario'].should == ""
	    end 

	end

	describe 'POST #cerrar' do
		it "Cerrar seccion con un token activo y su modelo y SO de celular correspondiente" do
			conexion = FactoryGirl.create(:conexionescelular)
			usuario = FactoryGirl.create(:user)
			params = { :token => "acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%", :id_usuario => 1, :modelo => "aaa", :so => "bbb" }
	        request.accept = "application/json"

		    post "cerrar", params 
		    resultado = JSON.parse(response.body)

			resultado[0]['status'].should == "OK"
		end

		it "Cerrar seccion con un token inactivo" do
			conexion = FactoryGirl.create(:conexionescelular)
			usuario = FactoryGirl.create(:user)
			params = { :token => "acY,%aYT8acY,%aYT8acY,%aYT812343,%aYT8acY,%aYT8acY,%", :id_usuario => 1, :modelo => "aaa", :so => "bbb" }
	        request.accept = "application/json"

		    post "cerrar", params 
		    resultado = JSON.parse(response.body)

			resultado[0]['status'].should == "No se encontro la seccion activa"
	    end

	    it "Cerrar seccion con un token activo y su modelo y SO de celular no correspondiente" do
			conexion = FactoryGirl.create(:conexionescelular)
			usuario = FactoryGirl.create(:user)
			params = { :token => "acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%", :id_usuario => 1, :modelo => "bbb", :so => "aa" }
	        request.accept = "application/json"

		    post "cerrar", params 
		    resultado = JSON.parse(response.body)

			resultado[0]['status'].should == "La información del token no coincide con las secciones que tiene activa"
		end
	end

end