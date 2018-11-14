package com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber.pinyin;


import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.ContactModel;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator2 implements Comparator<ContactModel.MembersEntity> {

	public int compare(ContactModel.MembersEntity o1, ContactModel.MembersEntity o2) {
		if(StringValid.isStringValid(o1.getSortLetters()) && StringValid.isStringValid(o2.getSortLetters())){
			if (o1.getSortLetters().equals("@")
					|| o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#")
					|| o2.getSortLetters().equals("@")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}else {
			return -1;
		}

	}

}
