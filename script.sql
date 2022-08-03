ALTER TABLE ator ADD CHECK (idgenero between 1 and 3),
				 ADD CHECK (idetnia between 1 and 4),
				 ADD CHECK (idfaixaetaria between 1 and 10),
				 ADD CHECK (idfaixapeso between 1 and 10),
				 ADD CHECK (idfaixaestatura between 1 and 10);

ALTER TABLE personagem ADD CHECK (idgenero between 1 and 3),
					   ADD CHECK (idetnia between 1 and 4),
					   ADD CHECK (idfaixaetaria between 1 and 10),
				       ADD CHECK (idfaixapeso between 1 and 10),
					   ADD CHECK (idfaixaestatura between 1 and 10);

ALTER TABLE autor ADD CHECK (idgenero between 1 and 3);

ALTER TABLE versaonovela ADD CHECK (qtdcapitulos >= 0);