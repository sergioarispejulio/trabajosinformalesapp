require "rails_helper"

RSpec.describe Generadortoken, :type => :model do

	describe 'Generar ascii de string' do
		it "Generar anscii de 1 char de string" do
		   	generador = Generadortoken.new("","",1)
		   	resultado = generador.convertir_a_ascii("a")
			expect(resultado).to eq("97")
		end

		it "Generar anscii de de un string" do
		   	generador = Generadortoken.new("","",1)
		   	resultado = generador.convertir_a_ascii("abcd")
			expect(resultado).to eq("979899100")
		end
	end

	describe 'Combinar modelo, sistema operativo y ci para crear un string de ascii' do
		it "Generar string de ascii" do
		   	generador = Generadortoken.new("aaa","bbb",123456)
		   	resultado = generador.generar_string_de_ascii
			expect(resultado).to eq("9799891237978984569799891237978984569799891237978984569799891237978984569799891237978984569799891237")
		end
	end

	describe "Dado un string de string de ascii, generar una palabra de 50 caracteres" do
		it "Generar string de ascii" do
		   	generador = Generadortoken.new("aaa","bbb",123456)
		   	resultado = generador.generar_string_de_ascii
		   	resultado = generador.convertir_de_ascii_a_palabra(resultado)
			expect(resultado).to eq("acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%")
		end
	end

end