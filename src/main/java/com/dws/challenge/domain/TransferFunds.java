package com.dws.challenge.domain;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class TransferFunds {
	
	@NotNull
	@NotEmpty
	private String fromAccountId;
	
	@NotNull
	@NotEmpty
	private String toAccountId;
	
	@NotNull
	@Min(value = 0, message = "Transfer amount must be positive.")
	private BigDecimal transferAmount;
}
