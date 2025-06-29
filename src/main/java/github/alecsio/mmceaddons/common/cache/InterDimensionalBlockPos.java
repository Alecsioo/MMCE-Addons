package github.alecsio.mmceaddons.common.cache;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.util.math.BlockPos;

@Desugar
public record InterDimensionalBlockPos(int dimensionId, BlockPos pos) {}
