ALTER TABLE ator ADD CHECK (id_genero between 1 and 3),
				 ADD CHECK (id_etnia between 1 and 4),
				 ADD CHECK (id_faixa_etaria between 1 and 10),
				 ADD CHECK (id_faixa_peso between 1 and 10),
				 ADD CHECK (id_faixa_estatura between 1 and 10);

ALTER TABLE personagem ADD CHECK (id_genero between 1 and 3),
					   ADD CHECK (id_etnia between 1 and 4),
					   ADD CHECK (id_faixa_etaria between 1 and 10),
				       ADD CHECK (id_faixa_peso between 1 and 10),
					   ADD CHECK (id_faixa_estatura between 1 and 10);

ALTER TABLE autor ADD CHECK (id_genero between 1 and 3);

ALTER TABLE versaonovela ADD CHECK (qtdcapitulos >= 0);