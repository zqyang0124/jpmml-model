/*
 * Copyright (c) 2011 University of Tartu
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;
import org.xml.sax.InputSource;

abstract
public class TransformationExample extends Example {

	@Parameter (
		names = {"--input"},
		description = "Input PMML file",
		required = true
	)
	private File input = null;

	@Parameter (
		names = {"--output"},
		description = "Output PMML file",
		required = true
	)
	private File output = null;


	abstract
	public PMML transform(PMML pmml);

	@Override
	public void execute() throws Exception {
		Unmarshaller unmarshaller = createUnmarshaller();
		Marshaller marshaller = createMarshaller();

		PMML pmml;

		InputStream is = new FileInputStream(this.input);

		try {
			Source source = ImportFilter.apply(new InputSource(is));

			pmml = (PMML)unmarshaller.unmarshal(source);
		} finally {
			is.close();
		}

		pmml = transform(pmml);

		OutputStream os = new FileOutputStream(this.output);

		try {
			Result result = new StreamResult(os);

			marshaller.marshal(pmml, result);
		} finally {
			os.close();
		}
	}
}