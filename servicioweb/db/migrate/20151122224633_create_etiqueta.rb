class CreateEtiqueta < ActiveRecord::Migration
  def change
    create_table :etiqueta do |t|
      t.string :id_tipo_de_trabajo
      t.string :nombre
      t.timestamps null: false
    end
  end
end
