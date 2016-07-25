require 'rails_helper'

RSpec.describe PuntageController, type: :controller do

	describe 'POST #Dar puntuación' do
	    
	     it "Verificar que se haya puntuado un perfil" do  
	      perfil = FactoryGirl.create(:perfiltrabajador)
	      params = { :puntaje => 6, :id_usuario_que_puntua => 2, :id_usuario_que_recibe_puntuacion => 1, :id_perfil_trabajador => 1 }
	      request.accept = "application/json"
	      post "dar_puntaje_a_trabajador", params 
	      resultado = JSON.parse(response.body)

	      resultado[0]['status'].should == "OK"
	    end


	    it "Ver que el promedio de puntaje del perfil cambie con 3 puntuaciones" do  
	      perfil = FactoryGirl.create(:perfiltrabajador)
	      params = { :puntaje => 6, :id_usuario_que_puntua => 2, :id_usuario_que_recibe_puntuacion => 1, :id_perfil_trabajador => 1 }
	      request.accept = "application/json"
	      post "dar_puntaje_a_trabajador", params 
	      resultado = JSON.parse(response.body)

	      Perfiltrabajador.first.puntaje_promedio.should == 6

	      params2 = { :puntaje => 8, :id_usuario_que_puntua => 3, :id_usuario_que_recibe_puntuacion => 1, :id_perfil_trabajador => 1 }
	      request.accept = "application/json"
	      post "dar_puntaje_a_trabajador", params2
	      resultado = JSON.parse(response.body)

	      Perfiltrabajador.first.puntaje_promedio.should == 7

	      params3 = { :puntaje => 4, :id_usuario_que_puntua => 4, :id_usuario_que_recibe_puntuacion => 1, :id_perfil_trabajador => 1 }
	      request.accept = "application/json"
	      post "dar_puntaje_a_trabajador", params3
	      resultado = JSON.parse(response.body)

	      Perfiltrabajador.first.puntaje_promedio.should == 6
	    end

  	end

end
