package br.net.twome.kd.db.types;

import java.sql.Timestamp;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.base.AbstractInstant;
import org.mentabean.sql.conditions.Between;
import org.mentabean.sql.param.ParamValue;

public class BetweenJoda extends Between {

	public BetweenJoda(AbstractInstant beginValue, AbstractInstant endValue) {
		super(beginValue, endValue);
		begin = beginValue == null ? null : new ParamValue(new Timestamp(beginValue.getMillis()));
		end = endValue == null ? null : new ParamValue(new Timestamp(endValue.getMillis()));
	}
	
	public BetweenJoda(LocalDate beginValue, LocalDate endValue) {
		super(beginValue, endValue);
		begin = beginValue == null ? null : new ParamValue(new Timestamp(beginValue.toDate().getTime()));
		end = endValue == null ? null : new ParamValue(new Timestamp(endValue.toDate().getTime()));
	}
	
	public BetweenJoda(LocalDateTime beginValue, LocalDateTime endValue) {
		super(beginValue, endValue);
		begin = beginValue == null ? null : new ParamValue(new Timestamp(beginValue.toDate().getTime()));
		end = endValue == null ? null : new ParamValue(new Timestamp(endValue.toDate().getTime()));
	}

}
