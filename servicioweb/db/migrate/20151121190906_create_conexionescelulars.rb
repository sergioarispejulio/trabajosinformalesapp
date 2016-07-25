class CreateConexionescelulars < ActiveRecord::Migration
  def change
    create_table :conexionescelulars do |t|
      t.string :token
      t.string :token_mensajeria
      t.string :modelo_celular
      t.string :sistema_operativo
      t.date :fecha_login
      t.date :fecha_logout
      t.integer :id_usuario
      t.timestamps null: false
    end
  end
end
