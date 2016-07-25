class CreateTrabajos < ActiveRecord::Migration
  def change
    create_table :trabajos do |t|
      t.string :nombre_solicitante
      t.string :titulo
      t.string :descripcion
      t.string :direccion
      t.string :url_foto
      t.string :ciudad
      t.date :fecha_de_requerimiento
      t.integer :id_tipo_trabajo
      t.integer :id_solicitante
      t.float :coordenadax
      t.float :coordenaday
      t.boolean :activo
      t.timestamps null: false
    end
  end
end
