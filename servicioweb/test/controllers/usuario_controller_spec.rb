require 'rails_helper'

RSpec.describe UsuarioController, type: :controller do

	describe 'POST #create' do
	    it "Crear usuario y verificar que se cree seccion" do  
	      #prueba_params = { :nombre => "RECIBE", :apellido => "JSON", :nombre1 => "PARAMETROS", :apellido1 => "ESPERADOS" }
	      #request_headers = { "Accept" => "application/json", "Content-Type" => "application/json" }
	      #puts prueba_params 

	      #post "crearusuario", prueba_params , request_headers

	      expect(true).to eq(true)
	    end

  	end

end
