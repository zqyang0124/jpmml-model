/*
 * Copyright (c) 2011 University of Tartu
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

import com.beust.jcommander.Parameter;

import org.xml.sax.*;

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
		PMML pmml;

		InputStream is = new FileInputStream(this.input);

		try {
			Source source = ImportFilter.apply(new InputSource(is));

			pmml = JAXBUtil.unmarshalPMML(source);
		} finally {
			is.close();
		}

		pmml = transform(pmml);

		OutputStream os = new FileOutputStream(this.output);

		try {
			Result result = new StreamResult(os);

			JAXBUtil.marshalPMML(pmml, result);
		} finally {
			os.close();
		}
	}
}