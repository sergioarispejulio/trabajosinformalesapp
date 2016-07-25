FactoryGirl.define do
  factory :tipotrabajo do
  	id  1
	  nombre  "Albañil"
	  descripcion  ""
  end

  factory :tipotrabajo1, class: Tipotrabajo do
    id  2
	  nombre  "Albañil1"
	  descripcion  ""
  end

end
