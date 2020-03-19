/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

@jakarta.xml.bind.annotation.XmlAccessorType (jakarta.xml.bind.annotation.XmlAccessType.PUBLIC_MEMBER)
@jakarta.xml.bind.annotation.XmlSchemaTypes (
		{ 	@jakarta.xml.bind.annotation.XmlSchemaType( name="anyURI", 
					type=java.lang.String.class ), 
			@jakarta.xml.bind.annotation.XmlSchemaType( name="date", 
					type=javax.xml.datatype.XMLGregorianCalendar.class ) 
		}
)
@jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapters(
		{	@jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter (
					value=jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter.class,
					type=javax.xml.namespace.QName.class
					) }
)
package javasoft.sqe.tests.java2schema.CustomizedMapping.packages.XmlAccessorType002;


