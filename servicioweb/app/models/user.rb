class User < ActiveRecord::Base
  authenticates_with_sorcery!

  validates_confirmation_of :password
  validates_presence_of :password, :on => :create
  validates_presence_of :telefono
  validates_presence_of :nickname

  def devolver_nombre_completo
  	return nombre + " " + apellido
  end
  
end
