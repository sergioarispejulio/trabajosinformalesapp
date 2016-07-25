class CreatePuntuacions < ActiveRecord::Migration
  def change
    create_table :puntuacions do |t|
      t.integer :puntaje, :limit => 2
      t.integer :id_usuario_que_puntua
      t.integer :id_usuario_que_recibe_puntuacion
      t.integer :id_perfil_trabajador
      t.timestamps null: false
    end
  end
end
