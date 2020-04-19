<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	document.getElementById('${param.piece_id}').onmousedown = function() {
		this.style.position = 'absolute';
		var self = this;
		var ref = self.getBoundingClientRect();
		var totalref = document.getElementById('00').getBoundingClientRect();
		var cx = 0;
		var cy = 0;
		document.onmousemove = function(e) {
			e = e || event;
			fixPageXY(e);
			self.style.left = e.pageX - ref.left - ${height/16} + 'px';
			self.style.top = e.pageY - ref.top - ${height/16} + 'px';
			cx = e.pageX - totalref.left;
			cy = e.pageY - totalref.top;
		}
		document.onmouseup = function() {
			document.onmousemove = null;
			setTarget(self.id, cx, cy);
		}
	};
	document.getElementById('${param.piece_id}').ondragstart = function() {
		return false;
	};
</script>
