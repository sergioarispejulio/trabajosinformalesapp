require 'rails_helper'

RSpec.describe PeticionController, type: :controller do

	describe 'POST #peticiones_a_solicitudes_de_trabajo' do

  		it "Mandar una petición a una solicitud de trabajo" do
  			params = { :id_trabajo => 1 , :id_usuario => 1}
	    	request.accept = "application/json"
	    	post "mandar_peticion_de_solicitud_de_trabajo", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['status'].should == "OK"
  		end

  		it "Mandar una petición duplicada de una solicitud de trabajo" do
  			params = { :id_trabajo => 1 , :id_usuario => 1}
	    	request.accept = "application/json"
	    	post "mandar_peticion_de_solicitud_de_trabajo", params 
	    	post "mandar_peticion_de_solicitud_de_trabajo", params 
	      	resultado = JSON.parse(response.body)

	      	resultado[0]['status'].should == "OK"
	      	aux = Solicitudtrabajo.all
	      	aux.length.should == 1
  		end

  		it "Aceptar una petición a una solicitud de trabajo" do
  			trabajo1 = FactoryGirl.create(:trabajo1)
  			solicitud1 = FactoryGirl.create(:solicitud_trabajo1)
  			params = { :id_solicitud => 1 , :resultado => true, :id_trabajo => 1}
	    	request.accept = "application/json"
	    	post "acepter_o_rechazar_peticion_de_solicitud_de_trabajo", params 
	      	resultado = JSON.parse(response.body)
			trabajo = Trabajo.first
	      	resultado[0]['status'].should == "OK"
	      	trabajo.activo.should == false
  		end

  		it "Negar una petición a una solicitud de trabajo" do
  			trabajo1 = FactoryGirl.create(:trabajo1)
  			solicitud1 = FactoryGirl.create(:solicitud_trabajo1)
  			params = { :id_solicitud => 1 , :resultado => false, :id_trabajo => 1}
	    	request.accept = "application/json"
	    	post "acepter_o_rechazar_peticion_de_solicitud_de_trabajo", params 
	      	resultado = JSON.parse(response.body)
			trabajo = Trabajo.first
	      	resultado[0]['status'].should == "OK"
	      	trabajo.activo.should == true
  		end

  	end



  	describe 'POST #obtener_mis_solicitudes_de_trabajos_publicados' do

  		it "Obtener una lista vacia de mis solicitudes de trabajo realizadas" do
  			params = { :id_trabajo => 1 }
	    	request.accept = "application/json"
	    	post "obtener_peticiones_de_la_solicitud", params 
	      	resultado = JSON.parse(response.body)

	      	resultado.length.should == 0
  		end

  		it "Obtener una lista con 1 de mis solicitude de trabajo realizada y sus postulantes" do
  			user = FactoryGirl.create(:user)
  			solicitud1 = FactoryGirl.create(:solicitud_trabajo1)
  			solicitud2 = FactoryGirl.create(:solicitud_trabajo2)
  			params = { :id_trabajo => 1 }
	    	request.accept = "application/json"
	    	post "obtener_peticiones_de_la_solicitud", params 
	      	resultado = JSON.parse(response.body)

	      	#.class para saber el tipo de dato
	      	resultado[0]['solicitud']['id'].should == 1
	      	resultado[1]['solicitud']['id'].should == 2
	      	resultado[0]['nombre_usuario'].should == "Nombre Apellido"
	      	#resultado[0]['nombre_usuario'].should == "Nombre Apellido"
	      	resultado.length.should == 2
  		end

  	end


  	describe 'POST #obtener_mis_solicitudes_de_trabajos_publicados' do

  		it "Obtener una lista con 1 de mis peticiones a solicitud de trabajos que hice" do
  			user = FactoryGirl.create(:user)
  			trabajo1 = FactoryGirl.create(:trabajo1)
  			trabajo2 = FactoryGirl.create(:trabajo2)
  			solicitud1 = FactoryGirl.create(:solicitud_trabajo1)
  			solicitud2 = FactoryGirl.create(:solicitud_trabajo2)
  			params = { :id_usuario => 1 }
	    	request.accept = "application/json"
	    	post "obtener_mis_peticiones", params 
	      	resultado = JSON.parse(response.body)
	      	resultado.length.should == 1
	      	resu = resultado[0]['nombre_trabajo'].include? "trabajo1"
	      	resu.should == true
  		end


  	end



end
