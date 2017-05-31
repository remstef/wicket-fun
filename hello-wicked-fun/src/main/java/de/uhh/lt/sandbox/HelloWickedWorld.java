package de.uhh.lt.sandbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class HelloWickedWorld extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final AbstractDefaultAjaxBehavior behave;

	public HelloWickedWorld() {

		add(new Label("message", "Hello World!"));
		add(new DropDownChoice<String>("dropdown", new PropertyModel<>(new Serializable() {String s="option2";}, "s"), Arrays.asList("option1", "option2")));
		List<String> choices = Arrays.asList("c1", "c2", "c3");
		add(new ListView<String>("checkboxes", choices){
			@Override
			protected void populateItem(ListItem<String> item) {
				
				CheckBox cb = new CheckBox("checkbox", new Model<Boolean>(Boolean.valueOf(true)));
				
				cb.add(new AjaxEventBehavior("onchange") {
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						cb.setModelObject(!cb.getModelObject());
						String js = String.format("alert('%s   %s')", item.getModelObject(), cb.getModelObject());
						target.appendJavaScript(js);
					}
				});
				item.add(cb);
				Label lbl = new Label("checkboxdesc", item.getModelObject());
//				lbl.add(new AjaxEventBehavior("onchange") {
//					@Override
//					protected void onEvent(AjaxRequestTarget target) {
//						String js = String.format("alert('%s   %s')", item.getModelObject(), cb.getModelObject());
//						target.appendJavaScript(js);
//					}
//				});
				item.add(lbl);
			}
		});
		


		Label l = new Label("cntr","0");
		l.setOutputMarkupId(true);
		add(l);
		
		behave = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			int c = 0;
			@Override
			protected void respond(AjaxRequestTarget target) {
				l.setDefaultModelObject(String.valueOf(++c));
				
				target.add(l);
				
				target.appendJavaScript("goToURL(event,'http://google.de','goog')");
			}
		};
		add(behave);
		System.out.println(behave.getCallbackUrl());
		add(new HiddenField<String>("callBackUrl", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			public String getObject() {
				return behave.getCallbackUrl().toString();
			}
		}));

		

		Label media = new Label("media", String.format("<source src='%s' type='%s' />", "http://hdl.handle.net/11022/0000-0000-5084-0@WEBM", "video/webm"));
		media.setEscapeModelStrings(false);
		add(media);

		List<String> list1 = Arrays.asList("a", "b", "c");
		List<Integer> list2 = Arrays.asList(1,2,3);

		if(Math.random() < 0.5){
			ListView<String> segments = new ListView<String>("segments", list1) {
				private static final long serialVersionUID = 1L;
				protected void populateItem(ListItem<String> item) {

					/* add link to list view */
					ExternalLink listref = new ExternalLink("listref", ".../annotate...#" + item.getModelObject(), item.getModelObject()); // TODO: replace by internal link
					item.add(listref);

					/* add link to play media */
					Image mediaref = new Image("mediaref", "pbn.gif");
					mediaref.add(new AttributeModifier("title", "00:00:01.33 - Click to start player"));
					mediaref.add(new AjaxEventBehavior("onclick") {
						private static final long serialVersionUID = 1L;
						@Override
						protected void onEvent(final AjaxRequestTarget target) {
							target.appendJavaScript("vidjump('1.23'); alert('onclick');");
						}
					});
					item.add(mediaref);

					/* add texts of speakers */
					ListView<Integer> speakertexts = new ListView<Integer>("segspeakertexts", list2) {
						private static final long serialVersionUID = 1L;
						@Override
						protected void populateItem(ListItem<Integer> subitem) {
							subitem.add(new Label("segspeakertext", subitem.getModelObject() + "(Wie)/ Ich kann s mich nur wiederholen:"));
						}
					};
					item.add(speakertexts);

					/* add annotations */
					ListView<Integer> annotationtexts = new ListView<Integer>("segannotationtexts", list2) {
						private static final long serialVersionUID = 1L;
						@Override
						protected void populateItem(ListItem<Integer> subitem) {
							subitem.add(new Label("segannotationtext", subitem.getModelObject() + "(Wie)/ Ich kann s mich nur wiederholen:"));
						}
					};
					item.add(annotationtexts);
				}
			};
			add(segments);
		}else{
			add(new ListView<String>("segments"){
				private static final long serialVersionUID = 1L;
				@Override
				protected void populateItem(ListItem<String> item) {/**/}
			});
		}

	}

}
