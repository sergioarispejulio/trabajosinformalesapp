require 'rails_helper'

RSpec.describe TrabajoController, type: :controller do

	describe 'POST #crear_trabajos' do
	    it "Crear trabajo" do  
        user1 = FactoryGirl.create(:user) 
	      params = { :ciudad => "Cochabamba", :titulo => "Trabajo prueba", :descripcion => "Se necesita plomero para repara una pileta", :coordenadax => 54.5, :coordenaday => -78.5, :fecha_de_requerimiento => "2014/5/5", :id_tipo_trabajo => 1, :id_solicitante => 1}
	    	request.accept = "application/json"
	    	post "crear", params 
	      resultado = JSON.parse(response.body)

	      resultado[0]['status'].should == "OK"
	    end

  end



  describe 'POST #obtener_mis_solicitudes_de_trabajos_publicados' do

  	it "Obtener una lista vacia de mis solicitudes de trabajo que publique" do
  		params = { :id_solicitante => 1}
	   	request.accept = "application/json"
	   	post "lista_publicaciones_realizadas", params 
      resultado = JSON.parse(response.body)

      resultado.length.should == 0
  	end

  	it "Obtener una lista con 1 de mis solicitude de trabajo que publique" do
  		user = FactoryGirl.create(:user)
  		trabajo = FactoryGirl.create(:trabajo1)
  		params = { :id_solicitante => 1}
	    request.accept = "application/json"
	    post "lista_publicaciones_realizadas", params 
	    resultado = JSON.parse(response.body)

	    resultado.length.should == 1
  	end

  	it "Obtener una lista con 4 de mis solicitudes de trabajo que publique" do
  		trabajo1 = FactoryGirl.create(:trabajo1)
  		trabajo2 = FactoryGirl.create(:trabajo2)
  		trabajo3 = FactoryGirl.create(:trabajo3)
  		trabajo4 = FactoryGirl.create(:trabajo4)
  		params = { :id_solicitante => 1 }
	   	request.accept = "application/json"
	   	post "lista_publicaciones_realizadas", params 
      resultado = JSON.parse(response.body)

      resultado.length.should == 4
 		end

 	end



 	describe 'POST #Buscar_solicitudes_de_trabajo' do
 		it "Obtener una lista vacia de solicitudes de trabajo de una determinada ciudad" do
 			params = { :id_tipo_trabajo => 1, :activo => true, :ciudad => "Cochabamba"}
    	request.accept = "application/json"
    	post "buscar_solicitudes_de_trabajo", params 
      resultado = JSON.parse(response.body)

      resultado.length.should == 0
 		end

 		it "Obtener una lista con 1 solicitud de de trabajo activa" do
 			trabajo = FactoryGirl.create(:trabajo1)
 			solicitud1 = FactoryGirl.create(:solicitud_trabajo1)
 			params = { :id_tipo_trabajo => 1, :activo => true, :ciudad => "Cochabamba"}
    	request.accept = "application/json"
    	post "buscar_solicitudes_de_trabajo", params 
      resultado = JSON.parse(response.body)

      resultado.length.should == 1
 		end

 		it "Obtener una lista con 3 solicitudes de trabajo activas" do
 			trabajo1 = FactoryGirl.create(:trabajo)
      trabajo1 = FactoryGirl.create(:trabajo1)
 			trabajo2 = FactoryGirl.create(:trabajo2)
 			trabajo3 = FactoryGirl.create(:trabajo3)
 			trabajo4 = FactoryGirl.create(:trabajo4)
 			trabajo5 = FactoryGirl.create(:trabajo5)
 			params = { :id_tipo_trabajo => 1, :activo => true, :ciudad => "Cochabamba"}
    	request.accept = "application/json"
    	post "buscar_solicitudes_de_trabajo", params 
      resultado = JSON.parse(response.body)

      resultado.length.should == 3
 		end

 	end

  describe 'POST #Buscar_trabajo' do

      it "Buscar trabajo por ID" do  
        trabajo1 = FactoryGirl.create(:trabajo1)
        params = { :id_trabajo => 1 }
        request.accept = "application/json"
        post "buscar_por_id", params 
        resultado = JSON.parse(response.body)

        resultado.length.should == 1
      end

  end


  describe 'POST # perfiles_de_trabajador' do

      it "Actualizar perfil" do  
        user1 = FactoryGirl.create(:user) 
        perfil1 = FactoryGirl.create(:perfiltrabajador) 
        perfil2 = FactoryGirl.create(:perfiltrabajador4)
        tipotrabajo1 = FactoryGirl.create(:tipotrabajo) 
        tipotrabajo2 = FactoryGirl.create(:tipotrabajo1) 


        params = { :id => perfil1.id, :habilitado => false }
        request.accept = "application/json"
        post "actualizar_perfil_trabajo", params 
        resultado = JSON.parse(response.body)

        Perfiltrabajador.find(1).habilitado.should == false
      end

      it "Obtener mis perfiles de trabajador" do
        user1 = FactoryGirl.create(:user) 
        perfil1 = FactoryGirl.create(:perfiltrabajador) 
        perfil2 = FactoryGirl.create(:perfiltrabajador4)
        params = { :id_usuario => 1 }
        request.accept = "application/json"
        post "obtener_perfiles_de_trabajador", params 
        resultado = JSON.parse(response.body)

        resultado.length.should == 2

      end

  end


end
