package com.o9paathshala.batches.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.o9paathshala.batches.dto.BatchDTO;
import com.o9paathshala.dao.DAO;
import com.o9paathshala.dao.DAOFactory;
import com.o9paathshala.dao.DBConstants;
import com.o9paathshala.dao.PreparedStatementDTO;
import com.o9paathshala.dao.SQLConstants;
import com.o9paathshala.dto.SessionDTO;


@WebServlet("/GetMyBatches")
public class GetMyBatches extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetMyBatches() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(GetMyBatches.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
		return;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		DAO dao = DAOFactory.getDAOObject();
		PreparedStatementDTO psDTO = new PreparedStatementDTO();
		List<PreparedStatementDTO> psList=new ArrayList<PreparedStatementDTO>();
		List<Map<String, Object>> resultList = null;
		List<BatchDTO> data=new ArrayList<BatchDTO>();
		Gson gson = new Gson();
		String jsonString=null;
		try{
			psDTO.setDataType(DBConstants.INTEGER);
			psDTO.setPosition(1);
			psDTO.setValue(((SessionDTO)session.getAttribute("user")).getId());
			psList.add(psDTO);
			resultList=dao.getAll(SQLConstants.GET_BATCHES_ON_FACULTY.replace("instituteid", ((SessionDTO)session.getAttribute("user")).getCurrentInstituteId().toString()),psList);
			if(null!=resultList){
				for(int i=0;i<resultList.size();i++){
					BatchDTO dto=new BatchDTO();
					dto.setId(Integer.parseInt(resultList.get(i).get("batch_id").toString()));
					dto.setName((String)resultList.get(i).get("batch_name"));
					data.add(dto);
				}
			}	
			jsonString = gson.toJson(data);
			
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}finally{
			out.println(jsonString);
			out.close();
		}
	}
}



